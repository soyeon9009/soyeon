package com.example.soyeon.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
public class BusDTO{
    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}

@Getter
class Response {
    private Header header;
    private Body body;
}

@Getter
class Header {
    private String resultCode;
    private String resultMsg;
}
@Getter
class Body {
    private int numOfRows;
    private int pageNo;
    private int totalCount;
    private List<Items> items;
}

@Getter
class Items {
    String area;
    String busnumber;
    String toawfirst;
    String toawlast;
    String t1endfirst;
    String t2endfirst;
    String t1endlast;
    String t2endlast;
    String adultfare;
    String busclass;
    String cpname;
    String t1wdayt;
    String t1wt;
    String t2wdayt;
    String t2wt;
    String routeinfo;
    String t1ridelo;
    String t2ridelo;
    String numOfRows;
    String pageNo;
    String totalCount;
    String resultCode;
    String resultMsg;
}
