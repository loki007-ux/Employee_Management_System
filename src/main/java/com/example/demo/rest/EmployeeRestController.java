package com.example.demo.rest;

import com.example.demo.entity.Employee;
import com.example.demo.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EmployeeRestController {
      private EmployeeService employeeService;
      private ObjectMapper objectMapper;

      @Autowired
      public EmployeeRestController(EmployeeService employeeService,ObjectMapper objectMapper){
          this.employeeService=employeeService;
          this.objectMapper=objectMapper;
      }


    //expose /employees and return a list of employees
    @GetMapping("/employee")
     public List<Employee> findAll(){

          return employeeService.findAll();
    }
    //find employee by id /GET method
    @GetMapping("/emolyee/{employeeId}")
    public Employee getEmployee(@PathVariable int employeeId){
          Employee employee=employeeService.findById(employeeId);
          if (employee == null){
              throw new RuntimeException("employee id not found " + employeeId);
          }
          return employee;
    }
    // add mapping of post , to create a new employee
    @PostMapping("/employee")
    public Employee addEmployee(@RequestBody Employee employee){
          //also just in case that they pass an id in json...set id to 0
        //this is to force save the item instead of updating it
          employee.setId(0);

          Employee dbEmployee=employeeService.save(employee);
          return  dbEmployee;

    }

    //add mapping of put ,to update an existing employee
    @PutMapping("/employee")
    public Employee updateEmployee(@RequestBody Employee employee){
          Employee dbEmployee=employeeService.save(employee);
          return dbEmployee;
    }

    //add mapping for patch to partially update
    @PatchMapping("/employee/{employeeId}")
    public Employee patchEmployee(@PathVariable int employeeId,
                                  @RequestBody Map<String, Object> patchPayload) {
        Employee employee=employeeService.findById(employeeId);
        if (employee==null){
            throw new RuntimeException("employee id not found " + employeeId);
        }
        // this exception is kept as we cant change the id which is primary key
        if (patchPayload.containsKey("id")){
            throw new RuntimeException("employee id cant be changed");
        }
        Employee patchedEmployee=apply(patchPayload ,employee);

        Employee dbEmployee=employeeService.save(patchedEmployee);
        return  dbEmployee;
    }

    private Employee apply(Map<String, Object> patchPayload, Employee employee) {
          //convert employees object to  JSON object node
        ObjectNode employeeNode=objectMapper.convertValue(employee,ObjectNode.class);
        //convert patchpayload map to JSON object node
        ObjectNode patchNode=objectMapper.convertValue(patchPayload,ObjectNode.class);
        //merge the patch updates in employee node
        employeeNode.setAll(patchNode);
        return  objectMapper.convertValue(employeeNode,Employee.class);

    }

    //add mapping for delete to delete employee
    @DeleteMapping("/employee/{employeeId}")
    public Employee deleteEmployee(@PathVariable int employeeId){
          Employee employee=employeeService.findById(employeeId);

          if(employee ==null){
              throw  new RuntimeException("employeee id doesnot exist " + employeeId);
          }
          employeeService.DeleteById(employeeId);
           return  employee;
    }
}
