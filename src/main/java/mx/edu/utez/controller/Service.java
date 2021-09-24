package mx.edu.utez.controller;

import mx.edu.utez.model.Employee;
import mx.edu.utez.util.ConnectionMysql;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Path("/employee")
public class Service {
    Connection con;
    PreparedStatement pstm;
    ResultSet rs;
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Employee> getEmployees(){
        List<Employee> employees = new ArrayList<>();
        Employee employee = new Employee();
        try {
            con = ConnectionMysql.getConnection();
            String query = "select employeeNumber,lastName,firstName,extension,email,officeCode, reportsTo,jobTitle from employees;";
            pstm = con.prepareStatement(query);
            rs = pstm.executeQuery();
            while (rs.next()){
                employee = new Employee();
                employee.setEmployeeNumber(rs.getInt("employeeNumber"));
                employee.setLastName(rs.getString("lastName"));
                employee.setFirstName(rs.getString("firstname"));
                employee.setExtension(rs.getString("extension"));
                employee.setEmail(rs.getString("email"));
                employee.setOfficeCode(rs.getInt("officecode"));
                employee.setReportsTo(rs.getInt("reportsTo"));
                employee.setJobTitle(rs.getString("jobTitle"));
                employees.add(employee);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            closeConnection();
        }
        return employees;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Employee getEmployee(@PathParam("id") int employeeNumber){
        Employee employee = new Employee();
        try {
            con = ConnectionMysql.getConnection();
            String query = "select employeeNumber,lastName,firstName,extension,email,officeCode, reportsTo,jobTitle from employees where employeeNumber = ?;";
            pstm = con.prepareStatement(query);
            pstm.setInt(1,employeeNumber);
            rs = pstm.executeQuery();
            while (rs.next()){
                employee = new Employee();
                employee.setEmployeeNumber(rs.getInt("employeeNumber"));
                employee.setLastName(rs.getString("lastName"));
                employee.setFirstName(rs.getString("firstname"));
                employee.setExtension(rs.getString("extension"));
                employee.setEmail(rs.getString("email"));
                employee.setOfficeCode(rs.getInt("officecode"));
                employee.setReportsTo(rs.getInt("reportsTo"));
                employee.setJobTitle(rs.getString("jobTitle"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            closeConnection();
        }
        return employee;
    }
    @GET
    @Path("/createEmployee/{employeeNumber}/{lastName}/{firstname}/{extension}/{email}/{officecode}/{reportsTo}/{jobTitle}")
    @Produces(MediaType.APPLICATION_JSON)
    public String createEmployee(@PathParam("employeeNumber") int employeeNumber,@PathParam("lastName") String lastName, @PathParam("firstname") String firstname, @PathParam("extension") String extension, @PathParam("email") String email,@PathParam("officecode") int officecode,@PathParam("reportsTo") int reportsTo, @PathParam("jobTitle") String jobTitle ){
        //EX: http://localhost:8080/rest_war_exploded/employee/createEmployee/2117/Saucedo/Miriam/X0309/20203tn052@utez.edu.mx/1/2121/Presidente
        boolean status = false;
        String text = "";
        try {
            con = ConnectionMysql.getConnection();
            String query = "insert into employees(employeeNumber,lastname,firstname,extension,email,officeCode,reportsTo,jobtitle) values(?,?,?,?,?,?,?,?);";
            pstm = con.prepareStatement(query);
            pstm.setInt(1,employeeNumber);
            pstm.setString(2,lastName);
            pstm.setString(3,firstname);
            pstm.setString(4,extension);
            pstm.setString(5,email);
            pstm.setInt(6,officecode);
            pstm.setInt(7,reportsTo);
            pstm.setString(8,jobTitle);
            status = pstm.executeUpdate()==1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if(status){
                text = "El registro fue correctamente insertado a la BD";
            }else{
                text = "Hubo un error al insertar a la BD";
            }
            closeConnection();
        }
        return text;
    }

    @GET
    @Path("/updateEmployee/{employeeNumber}/{lastName}/{firstname}/{extension}/{email}/{officecode}/{reportsTo}/{jobTitle}")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateEmployee(@PathParam("employeeNumber") int employeeNumber,@PathParam("lastName") String lastName, @PathParam("firstname") String firstname, @PathParam("extension") String extension, @PathParam("email") String email,@PathParam("officecode") int officecode,@PathParam("reportsTo") int reportsTo, @PathParam("jobTitle") String jobTitle ){
        //EX: http://localhost:8080/rest_war_exploded/employee/createEmployee/2117/Saucedo/Miriam/X0309/20203tn052@utez.edu.mx/1/2121/Presidente
        boolean status = false;
        String text = "";
        try {
            con = ConnectionMysql.getConnection();
            String query = "update employees set lastname=?,firstname=?,extension=?,email=?,officeCode=?,reportsTo=?,jobtitle=? where employeeNumber=?;";
            pstm = con.prepareStatement(query);
            pstm.setString(1,lastName);
            pstm.setString(2,firstname);
            pstm.setString(3,extension);
            pstm.setString(4,email);
            pstm.setInt(5,officecode);
            pstm.setInt(6,reportsTo);
            pstm.setString(7,jobTitle);
            pstm.setInt(8,employeeNumber);
            status = pstm.executeUpdate()==1;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if(status){
                text = "El registro fue correctamente modificado en la BD";
            }else{
                text = "Hubo un error al modificar en la BD";
            }
            closeConnection();
        }
        return text;
    }
    @GET
    @Path("/deleteEmployee/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteEmployee(@PathParam("id") int employeeNumber){
        boolean state = false;
        String text = "";
        try {
            con = ConnectionMysql.getConnection();
            String query = "delete from employees where employeenumber = ?;";
            pstm = con.prepareStatement(query);
            pstm.setInt(1,employeeNumber);
            state = pstm.executeUpdate() == 1;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            if(state){
                text = "El empleado fue correctamente eliminado";
            }else{
                text = "Hubo un problema al eliminar al empleado";
            }
            closeConnection();
        }
        return text;
    }
    public void closeConnection(){
        try{
            if(con != null){
                con.close();
            }
            if(pstm != null){
                pstm.close();
            }
            if(rs != null){
                rs.close();
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
    }
}