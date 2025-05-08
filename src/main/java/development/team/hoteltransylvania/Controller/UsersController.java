package development.team.hoteltransylvania.Controller;

import com.google.gson.Gson;
import development.team.hoteltransylvania.Business.GestionEmployee;
import development.team.hoteltransylvania.Business.GestionUser;
import development.team.hoteltransylvania.DTO.usersEmployeeDTO;
import development.team.hoteltransylvania.Model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "user", urlPatterns = {"/user"})
public class UsersController extends HttpServlet {

    GestionUser userdao = new GestionUser();
    User user = new User();

    GestionEmployee employeedao = new GestionEmployee();
    Employee employee = new Employee();

    usersEmployeeDTO usEmpDTO = new usersEmployeeDTO();

    int IdUsuario = 0;
    int IdEmployee = 0;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("get".equals(action)) {
            String idEmpleado = req.getParameter("idEmployee");
            usEmpDTO = employeedao.getEmployeeById(Integer.parseInt(idEmpleado));
            System.out.println(usEmpDTO.toString());

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            PrintWriter out = resp.getWriter();
            out.print(new Gson().toJson(usEmpDTO));
            out.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = req.getParameter("accion");

        switch (accion) {
            case "Listar":
                List<User> lista = userdao.getAllUsers();
                req.setAttribute("usuarios", lista);
                break;
            case "Registrar":
                //Para validar datos de inicio de sesión
                String name = req.getParameter("nombre");
                String email = req.getParameter("email");
                String username1 = req.getParameter("username");
                String num_doc = req.getParameter("numberdocumentHidden");
                String password1 = "123456";
                int idRol = Integer.parseInt(req.getParameter("rol"));

                if (userdao.existeUsuario(username1)) {
                    System.out.println("El email ya está registrado.");
                } else {
                    //AGREGAR A USUARIOS
                    user.setUsername(username1);
                    user.setPassword(password1);
                    //AGREGAR A EMPLEADOS
                    employee.setName(name);
                    if (idRol == 1) {
                        employee.setPosition("Administrador");
                    } else if (idRol == 2) {
                        employee.setPosition("Recepcionista");
                    } else {
                        employee.setPosition("rolEmpleado");
                    }
                    employee.setEmail(email);
                    employee.setNum_doc(num_doc);
                    IdEmployee = employeedao.registerEmployee(employee, idRol);
                    employee.setId(IdEmployee);

                    user.setEmployee(employee);
                    user.setStatusUser(StatusUser.valueOf("Activo"));
                    IdUsuario = userdao.registerUser(user);

                    System.out.printf("Se ha registrado el User con ID: " + IdUsuario);
                }
                resp.sendRedirect("menu.jsp?view=usuarios");
                return;
            case "update":
                int employeeId = Integer.parseInt(req.getParameter("idemployee"));
                int id = employeedao.getUserIdByEmployeeId(employeeId);
                User upUser = userdao.getUserById(id);
                String nombreEditar = req.getParameter("nombreEdit");
                String correoEditar = req.getParameter("correoEdit");
                String user = req.getParameter("userEdit");
                int rol = Integer.parseInt(req.getParameter("rolEditar"));
                String status = req.getParameter("estatusEdit");
                //ACTUALIZAR DATOS DE USUARIO
                upUser.setUsername(user);
                //ACTUALIZAR EMPLEADOS
                int empleadoId = upUser.getEmployee().getId();
                Employee upEmployee = userdao.obtenerEmpleadoPorId(empleadoId);
                upEmployee.setName(nombreEditar);
                if (rol == 1) {
                    upEmployee.setPosition("Administrador");
                } else if (rol == 2) {
                    upEmployee.setPosition("Recepcionista");
                } else {
                    upEmployee.setPosition("rolEmpleado");
                }
                upEmployee.setEmail(correoEditar);
                employeedao.updateEmployee(upEmployee, rol);

                upUser.setEmployee(upEmployee);
                upUser.setStatusUser(StatusUser.valueOf(status));
                userdao.updateUser(upUser);
                resp.sendRedirect("menu.jsp?view=usuarios");
                break;
            case "restartPass": //CUANDO SE LE DA AL BOTON DE ADMINISTRACION RESTABLECER A PREDETERMINADO
                int idUsuario = Integer.parseInt(req.getParameter("idUser"));
                User userReset = userdao.getUserById(idUsuario);
                userdao.updateUserPassword(userReset, "123456");
                System.out.println("Contraseña restablecida: ID USER: " + idUsuario);
                resp.sendRedirect("menu.jsp?view=usuarios");
                break;
            case "delete":
                int idUser = Integer.parseInt(req.getParameter("idUser"));
                userdao.updateStatus(idUser, "Inactivo");
                System.out.println("Usuario inactivado: ID: " + idUser);
                resp.sendRedirect("menu.jsp?view=usuarios");
                break;
            case "activate":
                int idU = Integer.parseInt(req.getParameter("idUser"));
                userdao.updateStatus(idU, "Activo");
                System.out.println("Usuario activado: ID: " + idU);
                resp.sendRedirect("menu.jsp?view=usuarios");
                break;
            case "updatePassword": //CAMBIAR CONTRASEÑA A NUEVA
                String newPassword = req.getParameter("newpassword");
                HttpSession sessionActual = req.getSession();
                User userLogin = (User) sessionActual.getAttribute("usuario");
                userLogin = userdao.updateUserPassword(userLogin, newPassword);
                sessionActual.setAttribute("usuario", userLogin);
                resp.sendRedirect("menu.jsp");
                break;
            default:
                break;
        }
    }
}

