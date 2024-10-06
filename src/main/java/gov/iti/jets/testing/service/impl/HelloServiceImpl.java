package gov.iti.jets.testing.service.impl;

import gov.iti.jets.testing.service.HelloService;
import org.springframework.stereotype.Service;

@Service
public class HelloServiceImpl implements HelloService {

    @Override
    public String getHello() {
        return "Hello";
    }
}
