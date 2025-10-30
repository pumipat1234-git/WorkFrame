// ...existing code...
import 'package:flutter/material.dart';
import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:labdb/place.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(debugShowCheckedModeBanner: false, home: LabDB());
  }
}

class LabDB extends StatefulWidget {
  const LabDB({super.key});

  @override
  State<LabDB> createState() => _LabDBState();
}

class _LabDBState extends State<LabDB> {
  TextEditingController ip_config = TextEditingController();
  TextEditingController uname = TextEditingController();
  TextEditingController pass = TextEditingController();
  bool closePassword = true;
  bool loginOK = false;
  String IP = '172.20.10.2';

  Future<bool> checkLogin(String uname, String pwd) async {
    try {
      final response = await http.get(
        Uri.parse(
          'http://${IP}/sarakhamdb/checkLogin.php?uname=$uname&pwd=$pwd',
        ),
      );

      if (response.statusCode == 200) {
        final jsonResponse = json.decode(response.body);
        if (jsonResponse['status'] == 'yes') {
          // นำทางไปหน้า PlacePage เมื่อล็อกอินสำเร็จ
          Navigator.pushReplacement(
            context,
            MaterialPageRoute(builder: (context) => PlacePage(IP: IP)),
          );
          loginOK = true;
        } else {
          loginOK = false;
        }
      } else {
        loginOK = false;
      }
    } catch (e) {
      loginOK = false;
    }
    return loginOK;
  }

  void showAlert(String msg, String tt) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: Text(tt, style: TextStyle(fontSize: 14)),
        content: Text(msg, style: TextStyle(fontSize: 16, color: Colors.blue)),
        actions: [
          TextButton(
            onPressed: () {
              Navigator.pop(context);
            },
            child: Text('ok'),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Login "), centerTitle: true),
      body: Container(
        width: double.infinity,
        height: double.infinity,
        padding: EdgeInsets.only(top: 20),
        child: SingleChildScrollView(
          child: Column(
            children: [
              SizedBox(height: 10),
              Container(
                padding: EdgeInsets.only(left: 30, right: 30),
                child: TextField(
                  controller: uname,
                  decoration: InputDecoration(
                    hintText: 'USERNAME',
                    border: OutlineInputBorder(
                      borderSide: BorderSide(color: Colors.blue),
                    ),
                  ),
                ),
              ),
              SizedBox(height: 12),
              Container(
                padding: EdgeInsets.only(left: 30, right: 30),
                child: TextField(
                  controller: pass,
                  obscureText: closePassword,
                  decoration: InputDecoration(
                    suffixIcon: IconButton(
                      onPressed: () {
                        setState(() {
                          closePassword = !closePassword;
                        });
                      },
                      icon: Icon(
                        closePassword ? Icons.visibility : Icons.visibility_off,
                      ),
                    ),
                    hintText: 'PASSWORD',
                    border: OutlineInputBorder(
                      borderSide: BorderSide(
                        color: const Color.fromARGB(255, 233, 24, 24),
                      ),
                    ),
                  ),
                ),
              ),
              SizedBox(height: 12),
              Container(
                padding: EdgeInsets.only(left: 30, right: 30),
                child: TextField(
                  controller: ip_config,
                  decoration: InputDecoration(
                    hintText: IP,
                    border: OutlineInputBorder(
                      borderSide: BorderSide(color: Colors.blue),
                    ),
                  ),
                ),
              ),
              SizedBox(height: 18),
              Container(
                padding: EdgeInsets.all(20),
                child: ElevatedButton(
                  onPressed: () {
                    // ถ้ามีกำหนดค่า IP ในช่อง ให้ใช้ค่านั้น
                    if (ip_config.text.isNotEmpty) {
                      IP = ip_config.text;
                    }

                    if (uname.text.isEmpty && pass.text.isEmpty) {
                      showAlert(
                        "กรุณาป้อน USERNAME และ PASSWORD",
                        "ป้อนข้อมูล",
                      );
                    } else if (uname.text.isNotEmpty && pass.text.isEmpty) {
                      showAlert("กรุณาป้อน PASSWORD ", "ป้อนข้อมูล");
                    } else if (uname.text.isEmpty && pass.text.isNotEmpty) {
                      showAlert("กรุณาป้อน USERNAME", "ป้อนข้อมูล");
                    } else {
                      checkLogin(uname.text, pass.text);
                    }
                  },
                  child: Text("LOGIN"),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
// ...existing code...