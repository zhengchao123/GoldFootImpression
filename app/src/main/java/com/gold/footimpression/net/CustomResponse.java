package com.gold.footimpression.net;

import okhttp3.Call;
import okhttp3.Response;

import java.io.IOException;

public interface CustomResponse {
     void onFailure(Call call, IOException e);

     void onResponse(Call call, Response response);
}
