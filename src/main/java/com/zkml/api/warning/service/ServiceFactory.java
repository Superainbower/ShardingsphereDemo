package com.zkml.api.warning.service;

import org.springframework.stereotype.Service;

@Service
public class ServiceFactory {

    private AbstractService abstractService;

    public void setAbstractService(AbstractService abstractService){
       this.abstractService = abstractService;
    }

    public AbstractService getAbstractService(){
        return abstractService;
    }

}
