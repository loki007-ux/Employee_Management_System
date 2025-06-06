package com.example.demo.service;

import com.example.demo.dao.EmployeeRepositary;
import com.example.demo.entity.Employee;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl  implements  EmployeeService{

    private EmployeeRepositary employeeRepositary;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepositary employeeRepositary){

        this.employeeRepositary=employeeRepositary;
    }
    @Override
    public List<Employee> findAll() {
        return employeeRepositary.findAll();
    }

    @Override
    public Employee findById(int id) {
        Optional<Employee> result=employeeRepositary.findById(id);
        Employee employee=null;

        if(result.isPresent()){
            employee=result.get();
        }else{
            throw new RuntimeException("employee id not found " + employee);

        }
        return  employee;
    }

    @Override
    public Employee save(Employee employee) {
        return employeeRepositary.save(employee);
    }


    @Override
    public void DeleteById(int id) {
      employeeRepositary.deleteById(id);
    }
}
