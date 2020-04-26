package com.example.rs485_demo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android_serialport_api.SerialPort;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity3 extends AppCompatActivity {

    private SerialPort mSerialPort;
    private FileInputStream mInputStream;
    private FileOutputStream mOutputStream;
    private TextView text_serial_port_status;
    private EditText edit_serial_port_name;
    private EditText edit_serial_port_baudrate;
    private Button button_disconnect_serial_port;
    private Button button_open_serial_port;
    private Button button_send_message;
    private EditText edit_send_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        try {
            /* 打开串口 */
            mSerialPort = new SerialPort(new File("/dev/ttyWK1"), 115200, 0);
            mOutputStream = (FileOutputStream) mSerialPort.getOutputStream();
            mInputStream = (FileInputStream) mSerialPort.getInputStream();
            Log.d("inputstream",mInputStream.toString());

            byte[] b = "helloworld".getBytes();
            mOutputStream.write(b);
            mOutputStream.flush();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            release();
        }
    }

    private void release() {
        try{
            mSerialPort.close();
            mInputStream.close();
            mOutputStream.close();
            mSerialPort=null;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
