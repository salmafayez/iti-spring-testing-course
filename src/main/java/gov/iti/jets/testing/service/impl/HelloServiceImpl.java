package gov.iti.jets.testing.service.impl;

import gov.iti.jets.testing.service.HelloService;
import org.springframework.stereotype.Service;

@Service
public class HelloServiceImpl implements HelloService {

    // alot of repos
    @Override
    public String sayHello() {
        //case
        return "Hello";
    }

    @Override
    public String sayHelloTo(String name) {
        return "Hello " + name;
    }

    @Override
    public void throwException() {
        throw new RuntimeException(" Hello");
    }
}
