/**
 * Created by Brenton on 3/21/2016.
 */
import java.io.*;
import java.util.*;
import java.net.*;

public class PopMail
{
    public static void main(String[] args)
    {
        PopMail pop = new PopMail();
        pop.retrieve();
    }

    public void retrieve()
    {
        try
        {
            String sub, oSub = "";
            String send = "";
            Socket s = new Socket("mail.mweb.co.za", 110);
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            System.out.println(in.readLine());
            out.writeBytes("USER \r\n");//Add Username
            System.out.println(in.readLine());
            out.writeBytes("PASS \r\n");//Add password
            System.out.println(in.readLine());
            out.writeBytes("stat\r\n");
            String resp = in.readLine();
            System.out.println(resp);
            resp = resp.substring(4,6);
            int num = Integer.parseInt(resp);
            System.out.println(num);
            for (int i = 34; i <= 35; i++)
            {
                out.writeBytes("retr " + i + "\r\n");
                sub = in.readLine();
                while(sub.contains("Subject")==false)
                {
                    System.out.println(sub);
                    sub = in.readLine();
                }
                if(sub.toUpperCase().contains("TEST"))
                {
                    oSub = sub;
                    while(sub.contains("--")==false)
                    {
                        sub = in.readLine();
                    }
                    if(sub.contains("--"))
                    {
                        sub = in.readLine();
                        sub = in.readLine();
                        while(sub.contains("--")==false)
                        {
                            send = send + sub;
                            sub = in.readLine();
                        }
                    }
                    sendMail(oSub, send);
                }

            }

            out.close();
            in.close();
            s.close();
        }
        catch(IOException e)
        {
            System.out.println("Failed");
        }
    }

    public void sendMail(String subject, String content)
    {
        try
        {
            Socket socket = new Socket("smtp.mweb.co.za", 25);
            DataOutputStream send  = new DataOutputStream(socket.getOutputStream());
            BufferedReader rec = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println(rec.readLine());
            send.writeBytes("HELO localhost\r\n");
            System.out.println(rec.readLine());
            send.writeBytes("EHLO localhost\r\n");
            System.out.println(rec.readLine());
            send.writeBytes("AUTH LOGIN\r\n");
            System.out.println(rec.readLine());
            send.writeBytes("\r\n");//Add username encoded into Base64
            System.out.println(rec.readLine());
            send.writeBytes("\r\n");//Add password encoded into Base64
            System.out.println(rec.readLine());
            send.writeBytes("MAIL FROM: \r\n");//Add a source email
            System.out.println(rec.readLine());
            send.writeBytes("RCPT TO: watt.brenton@gmail.com\r\n");
            System.out.println(rec.readLine());
            send.writeBytes("DATA\r\n");
            System.out.println(rec.readLine());
            send.writeBytes("From: \r\n");//Add a source email
            send.writeBytes("To: watt.brenton@gmail.com\r\n");
            send.writeBytes("Content-type: text/html\r\n");
            send.writeBytes(subject + "\r\n");
            send.writeBytes("\r\n");
            send.writeBytes(content + "\r\n");;
            send.writeBytes(".\r\n");
            System.out.println(rec.readLine());
        }
        catch(IOException e)
        {
            System.out.println("Failed 2");
        }
    }
}

