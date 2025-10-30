import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class PlaceImage {
  final String image_soure;

  PlaceImage({required this.image_soure});

  factory PlaceImage.fromJson(Map<String, dynamic> json) {
    return PlaceImage(image_soure: json['image_soure']);
  }
}

class PlaceDetail {
  final String place_id;
  final String place_name;
  final String place_detal;
  final String latitude;
  final String longitude;

  PlaceDetail({
    required this.place_id,
    required this.place_name,
    required this.place_detal,
    required this.latitude,
    required this.longitude,
  });

  factory PlaceDetail.fromJson(Map<String, dynamic> json) {
    return PlaceDetail(
      place_id: json['place_id'],
      place_name: json['place_name'],
      place_detal: json['place_detal'],
      latitude: json['latitude'],
      longitude: json['longitude'],
    );
  }
}

class PlaceDetailPage extends StatefulWidget {
  const PlaceDetailPage({
    super.key,
    required this.pid,
    required this.pname,
    required this.IP,
  });

  final String pid;
  final String pname;
  final String IP;

  @override
  State<PlaceDetailPage> createState() => _PlaceDetailPageState();
}

class _PlaceDetailPageState extends State<PlaceDetailPage> {
  late Future<List<PlaceDetail>> placeDetail;
  late Future<List<PlaceImage>> placeImage;

  Future<List<PlaceDetail>> getPlaceDetail(String pid) async {
    try {
      final response = await http.get(
        Uri.parse(
          'http://${widget.IP}/sarakhamdb/showPlaceDetail.php?pid=$pid',
        ),
      ); // ใส่ URL จริง

      if (response.statusCode == 200) {
        final List jsonResponse = json.decode(response.body);
        return jsonResponse.map((pdt) => PlaceDetail.fromJson(pdt)).toList();
      } else {
        // Server returned an error - log and return empty list to avoid throwing
        debugPrint('getPlaceDetail: server returned ${response.statusCode}');
        return <PlaceDetail>[];
      }
    } catch (e) {
      // Network or parsing error - log and return empty list
      debugPrint('getPlaceDetail error: $e');
      return <PlaceDetail>[];
    }
  }

  Future<List<PlaceImage>> getPlaceImage(String pid) async {
    try {
      final response = await http.get(
        Uri.parse('http://${widget.IP}/sarakham/showPlaceImage.php?pid=$pid'),
      ); // ใส่ URL จริง

      if (response.statusCode == 200) {
        final List jsonResponse = json.decode(response.body);
        return jsonResponse.map((pimg) => PlaceImage.fromJson(pimg)).toList();
      } else {
        debugPrint('getPlaceImage: server returned ${response.statusCode}');
        return <PlaceImage>[];
      }
    } catch (e) {
      debugPrint('getPlaceImage error: $e');
      return <PlaceImage>[];
    }
  }

  @override
  void initState() {
    super.initState();
    placeDetail = getPlaceDetail(widget.pid);
    placeImage = getPlaceImage(widget.pid);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('${widget.pname}'),
        backgroundColor: const Color.fromARGB(255, 246, 85, 155),
      ),
      body: FutureBuilder<List<PlaceDetail>>(
        future: placeDetail,
        builder: (context, dt) {
          if (dt.hasData) {
            return GridView.builder(
              padding: EdgeInsets.all(10),
              gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                crossAxisCount: 1, // 1 คอลัมน์
                crossAxisSpacing: 10,
                mainAxisSpacing: 10,
              ),
              itemCount: dt.data!.length,
              itemBuilder: (context, index) {
                var placeData = dt.data![index];
                return Container(
                  width: double.infinity,
                  height: double.infinity,
                  padding: EdgeInsets.all(20),
                  child: SingleChildScrollView(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        SizedBox(height: 20),
                        Text(placeData.place_detal),
                        SizedBox(height: 20),
                        Text(
                          'พิกัด : (${placeData.latitude}, ${placeData.longitude})',
                          style: TextStyle(color: Colors.red),
                        ),
                      ],
                    ),
                  ),
                );
              },
            );
          } else if (dt.hasError) {
            return Center(child: Text("Error: ${dt.error}"));
          }
          return Center(child: CircularProgressIndicator());
        },
      ),
    );
  }
}
