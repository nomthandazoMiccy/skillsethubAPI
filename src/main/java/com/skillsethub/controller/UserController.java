package com.skillsethub.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Properties;    
import javax.mail.*;    
import javax.mail.internet.*;  

import com.skillsethub.object.Email;
import com.skillsethub.object.Login;
import com.skillsethub.object.Skill;
import com.skillsethub.object.User;
//import com.sun.jersey.multipart.FormDataParam;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
//import com.sun.jersey.multipartFormDataContentDisposition;

@Path("/skillset")
public class UserController {

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String registerUser(Skill skill) {
		
		String message = "";
		if(skill != null) {
			
			try {
				int id = insertIntoUser(skill.getUser().getFirstName(),skill.getUser().getLastName(),skill.getUser().getEmail(),skill.getUser().getAddress(),skill.getUser().getPassword(),skill.getUser().getContact(),skill.getUser().getRole());
				System.out.println("The generated ID is " + id);
			    if(id != 0) {
			    	  
			    	 insertIntoSkills(skill.getProgrammingLanguage(), skill.getSkillDesc(),id, skill.getExperience(), skill.getJobTitle());
			     }
			     message = "User Registered Successfully";
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}else {
			message = "Something went wrong";
		}
		return message;
	}
	
	@POST
	@Path("/Login")
	@Produces(MediaType.APPLICATION_JSON)
	public int getUserLogin(Login login){
		 
		//String message = "try again";
		int role = 0;
		 try {
	    	  Class.forName("com.mysql.jdbc.Driver");
		      Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/skillsethubdb", "root", "Rita@12345!");
		  
		      String query ="SELECT * FROM users WHERE Email=? AND Password = ?";
		      PreparedStatement preparedStmt = conn.prepareStatement(query);
		  
		  
			  preparedStmt.setString(1,login.getEmail());
			  preparedStmt.setString(2,login.getPassword());

			  ResultSet resultSet =  preparedStmt.executeQuery();
			  
			 if(resultSet.next()) {
				 
				   role = resultSet.getInt("RoleId");
				   //message = "LoggedIn Successfully " + role;
			   }
	    	 }catch(Exception e) {
	    		 e.printStackTrace();
	    	 }
		 
		return role;
	  }
	
	 @GET
	 @Path("/{id}")
	 @Produces(MediaType.APPLICATION_JSON)
	 public List<Skill> getUserInfo(@PathParam("id") int id){
	 
		 List<Skill> skills = new ArrayList<Skill>();
		 
		 try {
		   	  Class.forName("com.mysql.jdbc.Driver");
		   	      Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/skillsethubdb", "root", "Rita@12345!");
		   	      
		   	      String sql = "SELECT * FROM users u INNER JOIN skills s ON u.UserId = s.UserId AND u.UserId = ?";
		   	      
		   	      PreparedStatement preparedStmt = conn.prepareStatement(sql);
			      preparedStmt.setInt(1,id);

				  ResultSet resultSet = preparedStmt.executeQuery();
		   	      
		   	      while(resultSet.next()) {
		   	    	  
		   	    	  Skill skill = new Skill(
		    	    		       resultSet.getString("ProgrammingLanguages"), 
		    	    		       resultSet.getString("SkillsDesc"),resultSet.getInt("UserId"),
		    	    		       resultSet.getString("Experience"),resultSet.getString("JobTitle"));
 	     
		   	    	  User user = new User(resultSet.getInt("UserId"),resultSet.getString("FirstName"), 
							      resultSet.getString("LastName"),resultSet.getString("Email"),
							      resultSet.getString("Password"),resultSet.getString("Address"),resultSet.getInt("RoleId"),resultSet.getString("Contact"));
				 
					 skill.setUser(user);
					 skills.add(skill);
		   	      }
			 }catch(Exception e) {
				 e.printStackTrace();
			 }
		 
		 return skills;
	 }
	
	 
	 @POST
	 @Path("/email")
	 @Produces(MediaType.APPLICATION_JSON)
	 public int sendEmail(Email email){
		 	    
	       String user="thandazor28@gmail.com"; 
	       String password="MiccyMtsweni@97";
	       String to = email.getSpEmail();
	       int count = 0;
	        //Get properties object    
	          Properties props = new Properties();    
	          props.put("mail.smtp.host", "smtp.gmail.com");    
	          props.put("mail.smtp.socketFactory.port", "465");    
	          props.put("mail.smtp.socketFactory.class",    
	                    "javax.net.ssl.SSLSocketFactory");    
	          props.put("mail.smtp.auth", "true");    
	          props.put("mail.smtp.port", "465");    
	          props.put("mail.smtp.starttls.enable", "true");
			  props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
	          props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

	          //get Session   
	           Session session = Session.getDefaultInstance(props,    
	           new javax.mail.Authenticator() {    
	           protected PasswordAuthentication getPasswordAuthentication() {    
	           return new PasswordAuthentication(user,password);  
	           }    
	          });    
	          //compose message    
	          try {    
	           MimeMessage message = new MimeMessage(session);    
	           message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));    
	           message.setSubject("Interested In Your Services");    
	           message.setText("Hi I would love to Hire you");    
	           //send message  
	           Transport.send(message); 
	           //Increment the clinetNo
	           try {
	              Class.forName("com.mysql.jdbc.Driver");
		   	      Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/skillsethubdb", "root", "Rita@12345!");
		   	      
		   	      String sql = "SELECT * FROM users WHERE Email = ?";
		   	      System.out.print("email " + to);
		   	      PreparedStatement preparedStmt = conn.prepareStatement(sql);
			      preparedStmt.setString(1, "thokozani@gmail.com");

				  ResultSet resultSet = preparedStmt.executeQuery();
		   	      System.out.println(resultSet);
		   	      if(resultSet.next()) {
		   	         
		   	    	 count = resultSet.getInt("InterestedClients") + 1;
		   	      }
		   	      String query = "UPDATE users SET InterestedClients = ? WHERE Email = ?";
		   	      PreparedStatement prepared = conn.prepareStatement(query);
		   	      prepared.setInt(1, count);
		   	      prepared.setString(2, "thokozani@gmail.com");
		   	      prepared.executeUpdate();
		   	      
	           }catch(Exception e) {
	        	   
	        	   e.printStackTrace();
	           }
	           System.out.println("Email sent successfully");
	           return count;    
	          } catch (MessagingException e) {
	        	  throw new RuntimeException(e);
	          }       
	    }
//get user using the email of the currently logged in user information
 @GET
 @Path("user/{email}")
 @Produces(MediaType.APPLICATION_JSON)
		 public List<Skill> getUserInfo(@PathParam("email") String email){
		 
			 List<Skill> skills = new ArrayList<Skill>();
			 
			 try {
			   	  Class.forName("com.mysql.jdbc.Driver");
			   	      Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/skillsethubdb", "root", "Rita@12345!");
			   	      
			   	      String sql = "SELECT * FROM users u INNER JOIN skills s ON u.UserId = s.UserId AND u.Email = ?";
			   	      
			   	      PreparedStatement preparedStmt = conn.prepareStatement(sql);
				      preparedStmt.setString(1,email);

					  ResultSet resultSet = preparedStmt.executeQuery();
			   	      
			   	      while(resultSet.next()) {
			   	    	  
			   	    	  Skill skill = new Skill(
			    	    		       resultSet.getString("ProgrammingLanguages"), 
			    	    		       resultSet.getString("SkillsDesc"),resultSet.getInt("UserId"),
			    	    		       resultSet.getString("Experience"),resultSet.getString("JobTitle"));
	 	     
			   	    	  User user = new User(resultSet.getInt("UserId"),resultSet.getString("FirstName"), 
								      resultSet.getString("LastName"),resultSet.getString("Email"),
								      resultSet.getString("Password"),resultSet.getString("Address"),resultSet.getInt("RoleId"),resultSet.getString("Contact"));
					 
						 skill.setUser(user);
						 skills.add(skill);
			   	      }
				 }catch(Exception e) {
					 e.printStackTrace();
				 }
			 
			 return skills;
		 }
		 
   ///update profile
    @PUT
	@Path("profile/{email}")
	@Produces(MediaType.APPLICATION_JSON)
	public String updateServiceProfile(@PathParam("email") String email,Skill skill){
		
		String message = "Failed to Update Profile";
		
		if(email != " ") {
			
			try {
				
				Class.forName("com.mysql.jdbc.Driver");
		   	    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/skillsethubdb", "root", "Rita@12345!");
		   	    
		   	   String query =  "UPDATE users u , skills s SET u.FirstName = ?, u.LastName = ? ,"
		  		             + "u.Contact = ?,u.Address = ?,s.JobTitle = ?,s.Experience = ?, "
		   			         + "s.ProgrammingLanguages = ?,s.SkillsDesc = ? "
		   			         + "WHERE u.UserId = s.UserId AND u.Email = ?";
		   	   
			  PreparedStatement preparedStmt = conn.prepareStatement(query);
			  
			  preparedStmt.setString(1,skill.getUser().getFirstName());
			  preparedStmt.setString(2,skill.getUser().getLastName());
			  preparedStmt.setString(3,skill.getUser().getContact());
			  preparedStmt.setString(4,skill.getUser().getAddress());
			  preparedStmt.setString(5,skill.getJobTitle());
			  preparedStmt.setString(6,skill.getExperience());
			  preparedStmt.setString(7,skill.getProgrammingLanguage());
			  preparedStmt.setString(8,skill.getSkillDesc());
		      preparedStmt.setString(9, email);

	          preparedStmt.executeUpdate();
	          
	          message = "User Profile Updated Successfully";
	          
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return message;
	}
    
    @POST
	@Path("/getRole")
	@Produces(MediaType.APPLICATION_JSON)
	public int getUserRole(User user){  
    	
    	int role = 0;
    	 try {
	    	  Class.forName("com.mysql.jdbc.Driver");
		      Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/skillsethubdb", "root", "Rita@12345!");
		  
		      String query ="SELECT * FROM users WHERE Email=? AND Password = ?";
		      PreparedStatement preparedStmt = conn.prepareStatement(query);
		  
		  
			  preparedStmt.setString(1,user.getEmail());
			  ResultSet resultSet =  preparedStmt.executeQuery();
			  
				 while(resultSet.next()) {
					 
					  role = resultSet.getInt(7);
				   }
    	 }catch(Exception e) {
    		 e.printStackTrace();
    	 }
    	
    	return role;
    }
/*    
@POST
@Path("/upload")
@Consumes(MediaType.MULTIPART_FORM_DATA)  
public Response uploadFile(@FormDataParam("file") InputStream uploadedInputStream,@FormDataParam("file") FormDataContentDisposition fileDetail){
	
        String fileLocation = "c://" + fileDetail.getFileName();  
        System.out.println("Location " + fileLocation);
        //saving file  
         try {  
                FileOutputStream out = new FileOutputStream(new File(fileLocation));  
                int read = 0;  
                byte[] bytes = new byte[1024];  
                out = new FileOutputStream(new File(fileLocation));  
                while ((read = uploadedInputStream.read(bytes)) != -1) {  
                    out.write(bytes, 0, read);  
                }  
                out.flush();  
                out.close();  
            } catch (IOException e) {
            	e.printStackTrace();
            }  
            String output = "File successfully uploaded to : " + fileLocation;  
          return Response.status(200).entity(output).build();  
 }  */ 
		 	
public static int insertIntoUser(String firstname,String lastname,String email,String address,String password,String contact,int RoleId){
       
	int userId = 0;
   	 try{
   			 Class.forName("com.mysql.jdbc.Driver");
   		     Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/skillsethubdb", "root", "Rita@12345!");
   		     
   		     PreparedStatement pst = conn.prepareStatement("INSERT INTO users(FirstName, LastName,Email,Address,Password,contact,RoleId)VALUES(?, ?, ?, ?,?,?,?);",Statement.RETURN_GENERATED_KEYS); 
   		     pst.setString(1, firstname);    
   		     pst.setString(2, lastname);
   		     pst.setString(3, email);
   		     pst.setString(4, address);
   		     pst.setString(5, password);
   		     pst.setString(6, contact);
   		     pst.setInt(7, RoleId);
   		     
            pst.executeUpdate(); 
            
            ResultSet rs = pst.getGeneratedKeys();
   	     while (rs.next()) {
   	    	 userId = rs.getInt(1); 
   	     }
         }catch(Exception e){
       	  e.printStackTrace();
         }
   	 
   	 return userId;
   }

public static void insertIntoSkills(String programmingLanguages,String skillsDesc,int userId,String experience,String jobTitle){
    
	 try{
			 Class.forName("com.mysql.jdbc.Driver");
			 Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/skillsethubdb", "root", "Rita@12345!");;
		     
		     PreparedStatement pst = conn.prepareStatement("INSERT INTO skillsethubdb.skills(ProgrammingLanguages, SkillsDesc,UserId,Experience,JobTitle)VALUES(?,?, ?, ?, ?);"); 
		     pst.setString(1, programmingLanguages);    
		     pst.setString(2, skillsDesc);
		     pst.setInt(3, userId);
		     pst.setString(4, experience);
		     pst.setString(5, jobTitle);
    
       pst.executeUpdate();  
     }catch(Exception e){
   	  e.printStackTrace();
     }
}
}
