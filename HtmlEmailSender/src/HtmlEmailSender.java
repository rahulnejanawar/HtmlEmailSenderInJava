 
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
 
public class HtmlEmailSender {
 
    public void sendHtmlEmail(String host, String port,
            final String userName, final String password, String toAddress,
            String subject, String message) throws AddressException,
            MessagingException {
 
        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.socketFactory.port", "465");
		properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.port", "465");
        /*properties.setProperty("mail.smtp.host", host);
        //properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.socketFactory.fallback", "false");
        properties.setProperty("mail.smtp.port", port);
        properties.setProperty("mail.smtp.socketFactory.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.debug", "true");
        properties.put("mail.store.protocol", "pop3");
        properties.put("mail.transport.protocol", "smtp");
        
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.debug", "true");
        */
 
        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
 
        Session session = Session.getInstance(properties, auth);
        System.out.println("session start.");
        // creates a new e-mail message
        Message msg = new MimeMessage(session);
        System.out.println("session created.");
        msg.setFrom(new InternetAddress(userName));
        InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        // set plain text message
        msg.setContent(message, "text/html");
 
        // sends the e-mail
        Transport.send(msg);
 
    }
 
    /**
     * Test the send html e-mail method
     * @throws SQLException 
     * @throws ClassNotFoundException 
     *
     */
    public void mailCreationPMApproval() throws ClassNotFoundException, SQLException{
    	// SMTP server information
        String host = "smtp.gmail.com";
        String port = "465";
        String mailFrom = "sendmail***@gmail.com";
        String password = "***sendmail***";
 
        // outgoing message information
        String mailTo = "***sendmail***@gmail.com";
        String subject = "PM Work Order Approval";
        Class.forName ("com.ddtek.jdbc.openedge.OpenEdgeDriver");
        //System.out.println("Driver loaded");
        Connection con = DriverManager.getConnection("jdbc:datadirect:openedge://localhost:2551;databaseName=db","db@123","db@123");
        //System.out.println("Connected");
        DatabaseMetaData md = con.getMetaData();
        
        ResultSet rs1 = md.getTables(null, null, "%", null);
        while (rs1.next()) {
          //System.out.println(rs1.getString(3));
        }
        Statement stmt = con.createStatement();
        ///System.out.println("Statement created");
        Statement stmt2 = con.createStatement();
        ResultSet rs3=stmt2.executeQuery("select * from PUB.Work_order");
        ResultSetMetaData rsmd = rs3.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while(rs3.next()){
        	for(int i=1;i<=columnsNumber;i++){
        		if (i > 1) System.out.print(",  ");
              //  String columnValue = rs3.getString(i);
                //System.out.print(columnValue + " " + rsmd.getColumnName(i)+""+i);
               // System.out.println(rs3.getString(7));
        	}
        }
        Statement stmt1 = con.createStatement();
        ResultSet rs2= stmt1.executeQuery("select Work_Supplier_Key,Extra_WS_info1, Work_supplier_name from PUB.Work_supplier");
        //System.out.println("ResultSet : \n");
        String message="";
        while(rs2.next()){
        //System.out.println("In rs2 loop ie WS");
        message += "<i>Greetings!</i><br>";
        message +="<head>";
        message +="<style>";
        message +="table, th, td {border: 1px solid black;border-collapse: collapse;}";
        message +="th {background-color:#dddddd;}";
        message +="</style>";
        message +="</head>";
        message +="<h1 align='center'><span>Work Order Management PM Approval</span></h1>";
        message +="<table>";
        message +="<tr>";
        message +="<th>Pos Key</th>";
        message +="<th>MO Key</th>";
        message +="<th>Short Description</th>";
        message +="<th>Wo Key</th>";
        message +="<th>Start Date</th>";
        message +="<th>Responsible</th>";
        message +="</tr>";
        
        ResultSet rs = stmt.executeQuery("select Pos_key,MO_key,WO_name,WO_Key,Start_date,Jobexec_date,Job_type,Finish_Work_Supplier_Key from PUB.Work_order order by WO_Key desc");
        //System.out.println("begin->"+rs.next());
        String messagebody="";
        while (rs.next())
        {
        	//System.out.println("in Loop rs ie WO");
        	if(rs.getString(7).equals("P")){
        		if(rs.getString(6)!=null){
        		if(rs.getString(8).equals(rs2.getString(1))){
        		//System.out.println(rs.getString(6)+" FW "+rs.getString(8)+" WS "+rs2.getString(1));
        		
        	messagebody +=("<tr><td>"+rs.getString(1)+"</td><td>"+rs.getString(2)+"</td><td>"+rs.getString(3)+"</td><td>"+rs.getString(4)+"</td><td>"+rs.getString(5)+"</td><td>"+rs2.getString(3)+"</td></tr>");
        	}
        	}
        	}
        	// message contains HTML markups
        }
        if(messagebody!=""){
        	message +=messagebody;
        //System.out.println("end->"+rs.next());
        
        message +="</table>";
        HtmlEmailSender mailer = new HtmlEmailSender();
        mailTo=rs2.getString(2);
        try {
        	System.out.println("Email begin.");
            mailer.sendHtmlEmail(host, port, mailFrom, password, mailTo,
                    subject, message);
            System.out.println("Email sent.");
            
        } catch (Exception ex) {
            System.out.println("Failed to sent email.");
            ex.printStackTrace();
        }
        }
        message=""; 
       }
    }
    
    public void mailCreationPMOverdue() throws ClassNotFoundException, SQLException, ParseException{
    	// SMTP server information
        String host = "smtp.gmail.com";
        String port = "465";
        String mailFrom = "sendmail***@gmail.com";
        String password = "***sendmail***";
        // outgoing message information
        String mailTo = "***sendmail***@gmail.com";
        String subject = "PM Work Order Overdue";
        Class.forName ("com.ddtek.jdbc.openedge.OpenEdgeDriver");
        //System.out.println("Driver loaded");
        Connection con = DriverManager.getConnection("jdbc:datadirect:openedge://localhost:2551;databaseName=db","db@123","db@123");
        //System.out.println("Connected");
        DatabaseMetaData md = con.getMetaData();
        
        ResultSet rs1 = md.getTables(null, null, "%", null);
        while (rs1.next()) {
          //System.out.println(rs1.getString(3));
        }
        Statement stmt = con.createStatement();
        ///System.out.println("Statement created");
        Statement stmt2 = con.createStatement();
        ResultSet rs3=stmt2.executeQuery("select * from PUB.Work_order");
        ResultSetMetaData rsmd = rs3.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while(rs3.next()){
        	for(int i=1;i<=columnsNumber;i++){
        		if (i > 1) System.out.print(",  ");
              //  String columnValue = rs3.getString(i);
                //System.out.print(columnValue + " " + rsmd.getColumnName(i)+""+i);
               // System.out.println(rs3.getString(7));
        	}
        }
        Statement stmt1 = con.createStatement();
        ResultSet rs2= stmt1.executeQuery("select Work_Supplier_Key,Extra_WS_info2,Work_Supplier_name from PUB.Work_supplier");
        //System.out.println("ResultSet : \n");
        String message="";
        while(rs2.next()){
        //System.out.println("In rs2 loop ie WS");
        message += "<i>Greetings!</i><br>";
        message +="<head>";
        message +="<style>";
        message +="table, th, td {border: 1px solid black;border-collapse: collapse;}";
        message +="th {background-color:#dddddd;}";
        message +="</style>";
        message +="</head>";
        message +="<h1 align='center'><span>Work Order Management PM Overdue</span></h1>";
        message +="<table>";
        message +="<tr>";
        message +="<th>Pos Key</th>";
        message +="<th>MO Key</th>";
        message +="<th>Short Description</th>";
        message +="<th>Wo Key</th>";
        message +="<th>Start Date</th>";
        message +="<th>Responsible</th>";
        message +="</tr>";
        
        ResultSet rs = stmt.executeQuery("select Pos_key,MO_key,WO_name,WO_Key,Start_date,End_date,Job_type,Work_Supplier_Key from PUB.Work_order order by WO_Key desc");
        //System.out.println("begin->"+rs.next());
        String messagebody="";
        
        while (rs.next())
        {
        	//System.out.println("in Loop rs ie WO");
        	if(rs.getString(7).equals("P")){
        		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String today_date = sdf.format(new java.util.Date());
                Date date1 = sdf.parse(rs.getString(6));
                Date date2 = sdf.parse(today_date);
                if (date1.compareTo(date2) < 0){
        		if(rs.getString(8).equals(rs2.getString(1))){
        		//System.out.println(rs.getString(4)+" FW "+rs.getString(8)+" WS "+rs2.getString(1));
        		
        	messagebody +=("<tr><td>"+rs.getString(1)+"</td><td>"+rs.getString(2)+"</td><td>"+rs.getString(3)+"</td><td>"+rs.getString(4)+"</td><td>"+rs.getString(5)+"</td><td>"+rs2.getString(3)+"</td></tr>");
        	}
        	}
        }
        	// message contains HTML markups
        }
        if(messagebody!=""){
        	message +=messagebody;
        //System.out.println("end->"+rs.next());
        
        message +="</table>";
        HtmlEmailSender mailer = new HtmlEmailSender();
        mailTo=rs2.getString(2);
        try {
        	System.out.println("Email begin.");
            mailer.sendHtmlEmail(host, port, mailFrom, password, mailTo,
                    subject, message);
            System.out.println("Email sent.");
            
        } catch (Exception ex) {
            System.out.println("Failed to sent email.");
            ex.printStackTrace();
        }
        }
        message=""; 
       }
    }
    
    public void mailCreationPMWarning() throws ClassNotFoundException, SQLException, ParseException{
    	// SMTP server information
    	String host = "smtp.gmail.com";
        String port = "465";
        String mailFrom = "sendmail***@gmail.com";
        String password = "***sendmail***";
        // outgoing message information
        String mailTo = "***sendmail***@gmail.com";
        String subject = "PM Work Order Notification";
        Class.forName ("com.ddtek.jdbc.openedge.OpenEdgeDriver");
        //System.out.println("Driver loaded");
        Connection con = DriverManager.getConnection("jdbc:datadirect:openedge://localhost:2551;databaseName=db","db@123","db@123");
        //System.out.println("Connected");
        DatabaseMetaData md = con.getMetaData();
        
        ResultSet rs1 = md.getTables(null, null, "%", null);
        while (rs1.next()) {
          //System.out.println(rs1.getString(3));
        }
        Statement stmt = con.createStatement();
        ///System.out.println("Statement created");
        Statement stmt2 = con.createStatement();
        ResultSet rs3=stmt2.executeQuery("select * from PUB.Work_order");
        ResultSetMetaData rsmd = rs3.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while(rs3.next()){
        	for(int i=1;i<=columnsNumber;i++){
        		if (i > 1) System.out.print(",  ");
              //  String columnValue = rs3.getString(i);
                //System.out.print(columnValue + " " + rsmd.getColumnName(i)+""+i);
               // System.out.println(rs3.getString(7));
        	}
        }
        Statement stmt1 = con.createStatement();
        ResultSet rs2= stmt1.executeQuery("select Work_Supplier_Key,Extra_WS_info2,Work_Supplier_name from PUB.Work_supplier");
        //System.out.println("ResultSet : \n");
        String message="";
        while(rs2.next()){
        //System.out.println("In rs2 loop ie WS");
        message += "<i>Greetings!</i><br>";
        message +="<head>";
        message +="<style>";
        message +="table, th, td {border: 1px solid black;border-collapse: collapse;}";
        message +="th {background-color:#dddddd;}";
        message +="</style>";
        message +="</head>";
        message +="<h1 align='center'><span>Work Order Management PM Notification</span></h1>";
        message +="<table>";
        message +="<tr>";
        message +="<th>Pos Key</th>";
        message +="<th>MO Key</th>";
        message +="<th>Short Description</th>";
        message +="<th>Wo Key</th>";
        message +="<th>Start Date</th>";
        message +="<th>Responsible</th>";
        message +="<th>Days Left</th>";
        message +="</tr>";
        
        ResultSet rs = stmt.executeQuery("select Pos_key,MO_key,WO_name,WO_Key,Start_date,End_date,Job_type,Work_Supplier_Key from PUB.Work_order order by WO_Key desc");
        //System.out.println("begin->"+rs.next());
        String messagebody="";
        
        while (rs.next())
        {
        	//System.out.println("in Loop rs ie WO");
        	if(rs.getString(7).equals("P")){
        		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String today_date = sdf.format(new java.util.Date());
                Date date1 = sdf.parse(rs.getString(5));
                Date date2 = sdf.parse(today_date);
                long diff = (date1.getTime()- date2.getTime());//in Milli seconds
                //long numOfDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                //int numOfDays = (int) (diff/(1000*60*60*24));
                
				long numOfDays = (diff / (1000*60*60*24));
				//System.out.println("today date "+date2+"  Startdate"+date1+"  Days Diff- "+numOfDays);
                if (numOfDays==1 || numOfDays==3 || numOfDays==7){
        		if(rs.getString(8).equals(rs2.getString(1))){
        		//System.out.println(rs.getString(4)+" FW "+rs.getString(8)+" WS "+rs2.getString(1));
        		
        	messagebody +=("<tr><td>"+rs.getString(1)+"</td><td>"+rs.getString(2)+"</td><td>"+rs.getString(3)+"</td><td>"+rs.getString(4)+"</td><td>"+rs.getString(5)+"</td><td>"+rs2.getString(3)+"</td><td>"+numOfDays+"</td></tr>");
        	}
        	}
        }
        	// message contains HTML markups
        }
        if(messagebody!=""){
        	message +=messagebody;
        //System.out.println("end->"+rs.next());
        
        message +="</table>";
        HtmlEmailSender mailer = new HtmlEmailSender();
        mailTo=rs2.getString(2);
        try {
        	System.out.println("Email begin.");
            mailer.sendHtmlEmail(host, port, mailFrom, password, mailTo,
                    subject, message);
            System.out.println("Email sent.");
            
        } catch (Exception ex) {
            System.out.println("Failed to sent email.");
            ex.printStackTrace();
        }
        }
        message=""; 
       }
    }
    
    public void mailCreationPMWarningApproval() throws ClassNotFoundException, SQLException, ParseException{
    	// SMTP server information
    	String host = "smtp.gmail.com";
        String port = "465";
        String mailFrom = "sendmail***@gmail.com";
        String password = "***sendmail***";
        // outgoing message information
        String mailTo = "***sendmail***@gmail.com";
        String subject = "PM Work Order Notification";
        Class.forName ("com.ddtek.jdbc.openedge.OpenEdgeDriver");
        //System.out.println("Driver loaded");
        Connection con = DriverManager.getConnection("jdbc:datadirect:openedge://localhost:2551;databaseName=db","db@123","db@123");
        //System.out.println("Connected");
        DatabaseMetaData md = con.getMetaData();
        
        ResultSet rs1 = md.getTables(null, null, "%", null);
        while (rs1.next()) {
          //System.out.println(rs1.getString(3));
        }
        Statement stmt = con.createStatement();
        ///System.out.println("Statement created");
        Statement stmt2 = con.createStatement();
        ResultSet rs3=stmt2.executeQuery("SELECT Pos_key,MO_key,MO_name,WO_Key,WO_name,Priority_key,MO_code_key,Job_type,Start_date, Start_time,End_date,End_time,Order_date,Description,Work_Supplier_Key,Jobcode_key,Stop_time,Finish_Work_Supplier_Key,Extra_WO_info1,Extra_WO_info2,Jobexec_date,Stop_end_date,Site_key,Plant_key,1 as source FROM PUB.Work_history WHERE Job_type = 'P' UNION SELECT Pos_key,MO_key,MO_name,WO_Key,WO_name,Priority_key,MO_code_key,Job_type,Start_date, Start_time,End_date,End_time,Order_date,Description,Work_Supplier_Key,Jobcode_key,Stop_time,Finish_Work_Supplier_Key,Extra_WO_info1,Extra_WO_info2,Jobexec_date,Stop_end_date,Site_key,Plant_key,0 as source FROM PUB.Work_order WHERE Job_type = 'P'");
        ResultSetMetaData rsmd = rs3.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while(rs3.next()){
        	for(int i=1;i<=columnsNumber;i++){
        		if (i > 1) System.out.print(",  ");
            //   String columnValue = rs3.getString(i);
            //    System.out.print(columnValue + " " + rsmd.getColumnName(i)+""+i);
            //   System.out.println(rs3.getString(7));
        	}
        }
        Statement stmt1 = con.createStatement();
        ResultSet rs2= stmt1.executeQuery("select Work_Supplier_Key,Extra_WS_info1,Work_Supplier_name from PUB.Work_supplier");
        //System.out.println("ResultSet : \n");
        String message="";
        while(rs2.next()){
        //System.out.println("In rs2 loop ie WS");
        message += "<i>Greetings!</i><br>";
        message +="<head>";
        message +="<style>";
        message +="table, th, td {border: 1px solid black;border-collapse: collapse;}";
        message +="th {background-color:#dddddd;}";
        message +="</style>";
        message +="</head>";
        message +="<h1 align='center'><span>Work Order Management PM Notification</span></h1>";
        message +="<table>";
        message +="<tr>";
        message +="<th>Pos Key</th>";
        message +="<th>MO Key</th>";
        message +="<th>Short Description</th>";
        message +="<th>Wo Key</th>";
        message +="<th>Start Date</th>";
        message +="<th>Responsible</th>";
        message +="<th>Days Left</th>";
        message +="</tr>";
        
        ResultSet rs = stmt.executeQuery("select Pos_key,MO_key,WO_name,WO_Key,Start_date,End_date,Job_type,Work_Supplier_Key from PUB.Work_order order by WO_Key desc");
        //System.out.println("begin->"+rs.next());
        String messagebody="";
        
        while (rs.next())
        {
        	//System.out.println("in Loop rs ie WO");
        	if(rs.getString(7).equals("P")){
        		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String today_date = sdf.format(new java.util.Date());
                Date date1 = sdf.parse(rs.getString(5));
                Date date2 = sdf.parse(today_date);
                long diff = (date1.getTime()- date2.getTime());//in Milli seconds
                //long numOfDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                //int numOfDays = (int) (diff/(1000*60*60*24));
                
				long numOfDays = (diff / (1000*60*60*24));
				//System.out.println("today date "+date2+"  Startdate"+date1+"  Days Diff- "+numOfDays);
                if (numOfDays==1 || numOfDays==3 || numOfDays==7){
        		if(rs.getString(8).equals(rs2.getString(1))){
        		//System.out.println(rs.getString(4)+" FW "+rs.getString(8)+" WS "+rs2.getString(1));
        		
        	messagebody +=("<tr><td>"+rs.getString(1)+"</td><td>"+rs.getString(2)+"</td><td>"+rs.getString(3)+"</td><td>"+rs.getString(4)+"</td><td>"+rs.getString(5)+"</td><td>"+rs2.getString(3)+"</td><td>"+numOfDays+"</td></tr>");
        	}
        	}
        }
        	// message contains HTML markups
        }
        if(messagebody!=""){
        	message +=messagebody;
        //System.out.println("end->"+rs.next());
        
        message +="</table>";
        HtmlEmailSender mailer = new HtmlEmailSender();
        mailTo=rs2.getString(2);
        try {
        	System.out.println("Email begin.");
            mailer.sendHtmlEmail(host, port, mailFrom, password, mailTo,
                    subject, message);
            System.out.println("Email sent.");
            
        } catch (Exception ex) {
            System.out.println("Failed to sent email.");
            ex.printStackTrace();
        }
        }
        message=""; 
       }
    }
    
    public void mailCreationCalWarning() throws ClassNotFoundException, SQLException, ParseException{
    	// SMTP server information
    	String host = "smtp.gmail.com";
        String port = "465";
        String mailFrom = "sendmail***@gmail.com";
        String password = "***sendmail***";
        // outgoing message information
        String mailTo = "***sendmail***@gmail.com";
        String subject = "CAL Work Order Notification";
        Class.forName ("com.ddtek.jdbc.openedge.OpenEdgeDriver");
        //System.out.println("Driver loaded");
        Connection con = DriverManager.getConnection("jdbc:datadirect:openedge://localhost:2551;databaseName=db","db@123","db@123");
        //System.out.println("Connected");
        DatabaseMetaData md = con.getMetaData();
        
        ResultSet rs1 = md.getTables(null, null, "%", null);
        while (rs1.next()) {
          //System.out.println(rs1.getString(3));
        }
        Statement stmt = con.createStatement();
        ///System.out.println("Statement created");
        Statement stmt2 = con.createStatement();
        ResultSet rs3=stmt2.executeQuery("SELECT Pos_key,MO_key,MO_name,WO_Key,WO_name,Priority_key,MO_code_key,Job_type,Start_date, Start_time,End_date,End_time,Order_date,Description,Work_Supplier_Key,Jobcode_key,Stop_time,Finish_Work_Supplier_Key,Extra_WO_info1,Extra_WO_info2,Jobexec_date,Stop_end_date,Site_key,Plant_key,1 as source FROM PUB.Work_history WHERE Job_type = 'P' UNION SELECT Pos_key,MO_key,MO_name,WO_Key,WO_name,Priority_key,MO_code_key,Job_type,Start_date, Start_time,End_date,End_time,Order_date,Description,Work_Supplier_Key,Jobcode_key,Stop_time,Finish_Work_Supplier_Key,Extra_WO_info1,Extra_WO_info2,Jobexec_date,Stop_end_date,Site_key,Plant_key,0 as source FROM PUB.Work_order WHERE Job_type = 'P'");
        ResultSetMetaData rsmd = rs3.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while(rs3.next()){
        	for(int i=1;i<=columnsNumber;i++){
        		if (i > 1) System.out.print(",  ");
            //   String columnValue = rs3.getString(i);
            //    System.out.print(columnValue + " " + rsmd.getColumnName(i)+""+i);
            //   System.out.println(rs3.getString(7));
        	}
        }
        Statement stmt1 = con.createStatement();
        ResultSet rs2= stmt1.executeQuery("select Work_Supplier_Key,Extra_WS_info2,Work_Supplier_name from PUB.Work_supplier");
        //System.out.println("ResultSet : \n");
        String message="";
        while(rs2.next()){
        //System.out.println("In rs2 loop ie WS");
        message += "<i>Greetings!</i><br>";
        message +="<head>";
        message +="<style>";
        message +="table, th, td {border: 1px solid black;border-collapse: collapse;}";
        message +="th {background-color:#dddddd;}";
        message +="</style>";
        message +="</head>";
        message +="<h1 align='center'><span>Work Order Management CAL Notification</span></h1>";
        message +="<table>";
        message +="<tr>";
        message +="<th>Pos Key</th>";
        message +="<th>MO Key</th>";
        message +="<th>MO Name</th>";
        message +="<th>Short Description</th>";
        message +="<th>Wo Key</th>";
        message +="<th>Start Date</th>";
        message +="<th>Due Date</th>";
        message +="<th>Responsible</th>";
        message +="<th>Days Left</th>";
        message +="</tr>";
        
        ResultSet rs = stmt.executeQuery("select Pos_key,MO_key,WO_name,WO_Key,Start_date,End_date,Job_type,Work_Supplier_Key,MO_name from PUB.Work_order order by WO_Key desc");
        //System.out.println("begin->"+rs.next());
        String messagebody="";
        
        while (rs.next())
        {
        	//System.out.println("in Loop rs ie WO");
        	if(rs.getString(7).equals("O")){
        		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String today_date = sdf.format(new java.util.Date());
                Date date1 = sdf.parse(rs.getString(6));
                Date date2 = sdf.parse(today_date);
                long diff = (date1.getTime()- date2.getTime());//in Milli seconds
                //long numOfDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                //int numOfDays = (int) (diff/(1000*60*60*24));
                
				long numOfDays = (diff / (1000*60*60*24));
				//System.out.println("today date "+date2+"  Startdate"+date1+"  Days Diff- "+numOfDays);
                if (numOfDays<=45 && numOfDays>=0){
        		if(rs.getString(8).equals(rs2.getString(1))){
        		//System.out.println(rs.getString(4)+" FW "+rs.getString(8)+" WS "+rs2.getString(1));
        		
        	messagebody +=("<tr><td>"+rs.getString(1)+"</td><td>"+rs.getString(2)+"</td><td>"+rs.getString(9)+"</td><td>"+rs.getString(3)+"</td><td>"+rs.getString(4)+"</td><td>"+rs.getString(5)+"</td><td>"+rs.getString(6)+"</td><td>"+rs2.getString(3)+"</td><td>"+numOfDays+"</td></tr>");
        	}
        	}
        }
        	// message contains HTML markups
        }
        if(messagebody!=""){
        	message +=messagebody;
        //System.out.println("end->"+rs.next());
        
        message +="</table>";
        HtmlEmailSender mailer = new HtmlEmailSender();
        mailTo=rs2.getString(2);
        try {
        	System.out.println("Email begin.");
            mailer.sendHtmlEmail(host, port, mailFrom, password, mailTo,
                    subject, message);
            System.out.println("Email sent.");
            
        } catch (Exception ex) {
            System.out.println("Failed to sent email.");
            ex.printStackTrace();
        }
        }
        message=""; 
       }
    }
    
    public void mailCreationCalWarningSummary() throws ClassNotFoundException, SQLException, ParseException{
    	// SMTP server information
    	String host = "smtp.gmail.com";
        String port = "465";
        String mailFrom = "sendmail***@gmail.com";
        String password = "***sendmail***";
        // outgoing message information
        String mailTo = "yiting_gan@jabil.com";
        String subject = "CAL Work Order Notification Summary";
        Class.forName ("com.ddtek.jdbc.openedge.OpenEdgeDriver");
        //System.out.println("Driver loaded");
        Connection con = DriverManager.getConnection("jdbc:datadirect:openedge://localhost:2551;databaseName=db","db@123","db@123");
        //System.out.println("Connected");
        DatabaseMetaData md = con.getMetaData();
        
        ResultSet rs1 = md.getTables(null, null, "%", null);
        while (rs1.next()) {
          //System.out.println(rs1.getString(3));
        }
        Statement stmt = con.createStatement();
        ///System.out.println("Statement created");
        Statement stmt2 = con.createStatement();
        ResultSet rs3=stmt2.executeQuery("SELECT Pos_key,MO_key,MO_name,WO_Key,WO_name,Priority_key,MO_code_key,Job_type,Start_date, Start_time,End_date,End_time,Order_date,Description,Work_Supplier_Key,Jobcode_key,Stop_time,Finish_Work_Supplier_Key,Extra_WO_info1,Extra_WO_info2,Jobexec_date,Stop_end_date,Site_key,Plant_key,1 as source FROM PUB.Work_history WHERE Job_type = 'P' UNION SELECT Pos_key,MO_key,MO_name,WO_Key,WO_name,Priority_key,MO_code_key,Job_type,Start_date, Start_time,End_date,End_time,Order_date,Description,Work_Supplier_Key,Jobcode_key,Stop_time,Finish_Work_Supplier_Key,Extra_WO_info1,Extra_WO_info2,Jobexec_date,Stop_end_date,Site_key,Plant_key,0 as source FROM PUB.Work_order WHERE Job_type = 'P'");
        ResultSetMetaData rsmd = rs3.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while(rs3.next()){
        	for(int i=1;i<=columnsNumber;i++){
        		if (i > 1) System.out.print(",  ");
            //   String columnValue = rs3.getString(i);
            //    System.out.print(columnValue + " " + rsmd.getColumnName(i)+""+i);
            //   System.out.println(rs3.getString(7));
        	}
        }
        Statement stmt1 = con.createStatement();
        ResultSet rs2= stmt1.executeQuery("select Work_Supplier_Key,Extra_WS_info2,Work_Supplier_name from PUB.Work_supplier");
        //System.out.println("ResultSet : \n");
        String message="";
        //while(rs2.next()){
        //System.out.println("In rs2 loop ie WS");
        message += "<i>Greetings!</i><br>";
        message +="<head>";
        message +="<style>";
        message +="table, th, td {border: 1px solid black;border-collapse: collapse;}";
        message +="th {background-color:#dddddd;}";
        message +="</style>";
        message +="</head>";
        message +="<h1 align='center'><span>Work Order Management CAL Notification Summary</span></h1>";
        message +="<table>";
        message +="<tr>";
        message +="<th>Pos Key</th>";
        message +="<th>MO Key</th>";
        message +="<th>MO Name</th>";
        message +="<th>Short Description</th>";
        message +="<th>Wo Key</th>";
        message +="<th>Start Date</th>";
        message +="<th>Due Date</th>";
        message +="<th>Responsible</th>";
        message +="<th>Days Left</th>";
        message +="</tr>";
        
        ResultSet rs = stmt.executeQuery("select Pos_key,MO_key,WO_name,WO_Key,Start_date,End_date,Job_type,Work_Supplier_Key,MO_name from PUB.Work_order order by WO_Key desc");
        //System.out.println("begin->"+rs.next());
        String messagebody="";
        
        while (rs.next())
        {
        	//System.out.println("in Loop rs ie WO");
        	if(rs.getString(7).equals("O")){
        		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String today_date = sdf.format(new java.util.Date());
                Date date1 = sdf.parse(rs.getString(6));
                Date date2 = sdf.parse(today_date);
                long diff = (date1.getTime()- date2.getTime());//in Milli seconds
                //long numOfDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                //int numOfDays = (int) (diff/(1000*60*60*24));
                
				long numOfDays = (diff / (1000*60*60*24));
				//System.out.println("today date "+date2+"  Startdate"+date1+"  Days Diff- "+numOfDays);
                if (numOfDays<=45 && numOfDays>=0){
        		//if(rs.getString(8).equals(rs2.getString(1))){
        		//System.out.println(rs.getString(4)+" FW "+rs.getString(8)+" WS "+rs2.getString(1));
        		
        	messagebody +=("<tr><td>"+rs.getString(1)+"</td><td>"+rs.getString(2)+"</td><td>"+rs.getString(9)+"</td><td>"+rs.getString(3)+"</td><td>"+rs.getString(4)+"</td><td>"+rs.getString(5)+"</td><td>"+rs.getString(6)+"</td><td>"+rs2.getString(3)+"</td><td>"+numOfDays+"</td></tr>");
        	//}
        	}
        }
        	// message contains HTML markups
        }
        if(messagebody!=""){
        	message +=messagebody;
        //System.out.println("end->"+rs.next());
        
        message +="</table>";
        HtmlEmailSender mailer = new HtmlEmailSender();
        //mailTo=rs2.getString(2);
        try {
        	System.out.println("Email begin.");
            mailer.sendHtmlEmail(host, port, mailFrom, password, mailTo,
                    subject, message);
            System.out.println("Email sent.");
            
        } catch (Exception ex) {
            System.out.println("Failed to sent email.");
            ex.printStackTrace();
        }
        }
        message=""; 
      // }
    }
    
    
    public static void main(String[] args) throws SQLException, ClassNotFoundException, ParseException {
    	HtmlEmailSender mailFormPMApproval = new HtmlEmailSender();
    	mailFormPMApproval.mailCreationPMApproval();
    	HtmlEmailSender mailFormPMOverdue = new HtmlEmailSender();
    	mailFormPMOverdue.mailCreationPMOverdue();
    	HtmlEmailSender mailFormPMWarning = new HtmlEmailSender();
    	mailFormPMWarning.mailCreationPMWarning();
    	HtmlEmailSender mailFormPMWarningApproval = new HtmlEmailSender();
    	mailFormPMWarningApproval.mailCreationPMWarningApproval();
    	Date now = new Date();
    	SimpleDateFormat simpleDateformat = new SimpleDateFormat("E"); // the day of the week abbreviated
        //System.out.println(simpleDateformat.format(now));
        //String Weekday = simpleDateformat.format(now);
        //System.out.println("Weekday"+Weekday);
        if (simpleDateformat.format(now).equals("Wed")){
    	HtmlEmailSender mailFormCalWarning = new HtmlEmailSender();
    	mailFormCalWarning.mailCreationCalWarning();
    	HtmlEmailSender mailFormCalWarningSummary = new HtmlEmailSender();
    	mailFormCalWarningSummary.mailCreationCalWarningSummary();
        }
    	
       
    }
}