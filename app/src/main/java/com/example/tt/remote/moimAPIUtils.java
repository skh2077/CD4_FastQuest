package com.example.tt.remote;

public class moimAPIUtils {
    private moimAPIUtils(){
    }

    public static final String API_URL = "http://52.79.125.108/api/assemble/";

    public static FileService getFileService(){
        return RetrofitClient.getClient(API_URL).create(FileService.class);
    }
}
