package com.example.rs485_demo;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android_serialport_api.SerialPort;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

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
    private String text="ttyWK1";
    private int baudrate = 115200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_open_serial_port = findViewById(R.id.button_open_serial_port);
        button_disconnect_serial_port = findViewById(R.id.button_disconnect_serial_port);
        text_serial_port_status= findViewById(R.id.text_serial_port_status);
        edit_serial_port_name = findViewById(R.id.edit_serial_port_name);
        edit_serial_port_baudrate = findViewById(R.id.edit_serial_port_baudrate);
        edit_send_message = findViewById(R.id.edit_send_message);
        button_send_message = findViewById(R.id.button_send_message);

        initButton();
    }

    private void initButton() {
        //打开串口按键
        button_open_serial_port.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text =edit_serial_port_name.getText().toString().trim().isEmpty()?null:edit_serial_port_name.getText().toString();
                Integer baudrate = edit_serial_port_baudrate.getText().toString().trim().isEmpty()?null:Integer.parseInt(edit_serial_port_baudrate.getText().toString());
                if(mSerialPort==null){
                    initSerialPort(text,baudrate);
                    setSerialPortStatus(true);
                }else {
                    Toast.makeText(MainActivity.this,"串口已连接",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //断开串口按键
        button_disconnect_serial_port.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSerialPort!=null){
                    setSerialPortStatus(false);
                }else{
                    Toast.makeText(MainActivity.this,"当前没有连接串口",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //发送串口信息按键
        button_send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSerialPort!=null){
                    sendMeassge();
                }else{
                    Toast.makeText(MainActivity.this,"当前没有连接串口",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //发送串口信息
    private void sendMeassge() {
        String message = edit_send_message.getText().toString();
        if(message.trim().isEmpty()){
            Toast.makeText(MainActivity.this,"不能发送空信息",Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            byte[] b = message.getBytes();
            System.out.println();
            System.out.println();
            Toast.makeText(MainActivity.this,"发送的数据:" + b+"\n"+"发出字节数：" + b.length,Toast.LENGTH_SHORT).show();
            mOutputStream.write(b);
            mOutputStream.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            mSerialPort.close();
            e.printStackTrace();
        }
    }

    private void setSerialPortStatus(boolean b) {
        if(b){
            text_serial_port_status.setText("已连接");
        }else{
            text_serial_port_status.setText("已断开");
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

    private void initSerialPort(String text,Integer baudrate) {
        if(text!=null&&baudrate!=null){
            this.text = text;
            this.baudrate = baudrate;
        }
        if(text!=null){
            this.text = text;
        }
        if(baudrate!=null){
            this.baudrate = baudrate;
        }
        //series
        try {
            /* 打开串口 */
            mSerialPort = new SerialPort(new File("/dev/" + this.text), this.baudrate, 0);
            mOutputStream = (FileOutputStream) mSerialPort.getOutputStream();
            mInputStream = (FileInputStream) mSerialPort.getInputStream();
            Toast.makeText(MainActivity.this,"串口连接成功,串口文件:"+"/dev/" + this.text+",波特率:"+ this.baudrate,Toast.LENGTH_SHORT).show();
            Log.d("inputstream",mInputStream.toString());

        } catch (SecurityException e) {
            Toast.makeText(MainActivity.this,"连接串口失败:SecurityException",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(MainActivity.this,"连接串口失败:IOException",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
