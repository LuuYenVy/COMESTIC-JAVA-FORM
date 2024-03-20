
package UserInterFace;

public class Detail {
    private String user;
    private String name;
    private int employeeid;
    public Detail(){
        user="dts1";
        name="Điện Tử Số 1";
        employeeid=0;
    }
    
    public Detail(String us, String na,int id){
        this.user=us;
        this.name=na;
        this.employeeid=id;
    }

    public Detail(Detail detail){
        this.user=detail.user;
        this.name=detail.name;
        this.employeeid=detail.employeeid;
    }
    
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }
    public int getID(){
        return employeeid;
    }
    public void setName(String name) {
        this.name = name;
    }
}
