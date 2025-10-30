// ...existing code...
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:shared_preferences/shared_preferences.dart';

import 'package:labdb/placedetail.dart';

class Place {
  final String id;
  final String name;
  final String image;

  Place({required this.id, required this.name, required this.image});

  factory Place.fromJson(Map<String, dynamic> json) {
    return Place(
      id: json['place_id'],
      name: json['place_name'],
      image: json['image_soure'],
    );
  }
}

class PlacePage extends StatefulWidget {
  const PlacePage({super.key, required this.IP});

  final String IP;

  @override
  State<PlacePage> createState() => _PlacePageState();
}

class _PlacePageState extends State<PlacePage> {
  late Future<List<Place>> place;
  Set<String> favorites = {};
  int _selectedIndex = 0; // 0 = หน้าแรก, 1 = รายการโปรด

  Future<List<Place>> getPlaceData() async {
    final response = await http.get(
      Uri.parse('http://${widget.IP}/sarakhamdb/showPlace.php'),
    ); // ใส่ URL จริง

    if (response.statusCode == 200) {
      List jsonResponse = json.decode(response.body);
      return jsonResponse.map((p) => Place.fromJson(p)).toList();
    } else {
      throw Exception("Failed to load data");
    }
  }

  Future<void> loadFavorites() async {
    final prefs = await SharedPreferences.getInstance();
    final List<String>? favList = prefs.getStringList('favorites');
    setState(() {
      favorites = favList != null ? Set<String>.from(favList) : {};
    });
  }

  Future<void> saveFavorites() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setStringList('favorites', favorites.toList());
  }

  void toggleFavorite(String id) {
    setState(() {
      if (favorites.contains(id)) {
        favorites.remove(id);
      } else {
        favorites.add(id);
      }
    });
    saveFavorites();
  }

  @override
  void initState() {
    super.initState();
    place = getPlaceData();
    loadFavorites();
  }

  Widget buildGrid(List<Place> data, {bool showOnlyFavorites = false}) {
    final List<Place> list = showOnlyFavorites
        ? data.where((p) => favorites.contains(p.id)).toList()
        : data;
    if (showOnlyFavorites && list.isEmpty) {
      return Center(child: Text('ยังไม่มีรายการโปรด'));
    }
    return GridView.builder(
      padding: EdgeInsets.all(10),
      gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
        crossAxisCount: 2, // 2 คอลัมน์
        crossAxisSpacing: 10,
        mainAxisSpacing: 10,
      ),
      itemCount: list.length,
      itemBuilder: (context, index) {
        var placeData = list[index];
        return InkWell(
          onTap: () {
            // เปิดรายละเอียด
            Navigator.push(
              context,
              MaterialPageRoute(
                builder: (context) => PlaceDetailPage(
                  pid: placeData.id,
                  pname: placeData.name,
                  IP: widget.IP,
                ),
              ),
            );
          },
          child: Card(
            child: Column(
              children: [
                Expanded(
                  child: Stack(
                    children: [
                      Positioned.fill(
                        child: Image.network(
                          'http://${widget.IP}/sarakhamdb/place_images/${placeData.image}',
                          fit: BoxFit.cover,
                        ),
                      ),
                      Positioned(
                        top: 4,
                        right: 4,
                        child: IconButton(
                          icon: Icon(
                            favorites.contains(placeData.id)
                                ? Icons.star
                                : Icons.star_border,
                            color: favorites.contains(placeData.id)
                                ? Colors.yellow
                                : Colors.white,
                          ),
                          onPressed: () {
                            toggleFavorite(placeData.id);
                            // ให้ feedback สั้น ๆ
                            ScaffoldMessenger.of(context).showSnackBar(
                              SnackBar(
                                content: Text(
                                  favorites.contains(placeData.id)
                                      ? 'เพิ่มในรายการโปรด'
                                      : 'ย้ายออกจากรายการโปรด',
                                ),
                                duration: Duration(milliseconds: 700),
                              ),
                            );
                          },
                        ),
                      ),
                    ],
                  ),
                ),
                Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: Text(placeData.name),
                ),
              ],
            ),
          ),
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('แนะนำสารคาม'),
        backgroundColor: Colors.yellow,
        centerTitle: true,
      ),
      body: FutureBuilder<List<Place>>(
        future: place,
        builder: (context, snapshot) {
          if (snapshot.hasData) {
            if (_selectedIndex == 0) {
              // หน้าแรก
              return buildGrid(snapshot.data!);
            } else {
              // รายการโปรด
              return buildGrid(snapshot.data!, showOnlyFavorites: true);
            }
          } else if (snapshot.hasError) {
            return Center(child: Text("Error: ${snapshot.error}"));
          }
          return Center(child: CircularProgressIndicator());
        },
      ),
      bottomNavigationBar: BottomNavigationBar(
        currentIndex: _selectedIndex,
        onTap: (i) {
          setState(() {
            _selectedIndex = i;
          });
        },
        items: [
          BottomNavigationBarItem(icon: Icon(Icons.home), label: 'หน้าแรก'),
          BottomNavigationBarItem(icon: Icon(Icons.star), label: 'รายการโปรด'),
        ],
      ),
    );
  }
}
// ...existing code...