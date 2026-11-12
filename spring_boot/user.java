package main.java.ArgusScanner.argusScanner.Model;


public class User{
    
    private Long userID;
    private String password_hash;
    private String name;
    private String email;
    private String role;

    public User(){
    }
        public User(Long userID, String password_hash, String name, String email, String role){
            this.userID = userID;
            this.password_hash = password_hash;
            this.name = name;
            this.email = email;
            this.role = role;
        }
        public Long getUserID(){
            return userID;
        }
        public void setUserID(Long userID){
            this.userID = userID;
        }
        public String getPasswordHash(){
            return password_hash;
        }
        public void setPasswordHash(String password_hash){
            this.password_hash = password_hash;
        }
        public String getName(){
            return name;
        }
        public void setName(String name){
            this.name = name;
        }
        public String getEmail(){
            return email;
        }
        public void setEmail(String email){
            this.email = email;
        }
        public String getRole(){
            return role;
        }
        public void setRole(String role){
            this.role = role;
        }
    }
                
            
        
    

                
