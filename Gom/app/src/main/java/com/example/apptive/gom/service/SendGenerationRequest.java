package com.example.apptive.gom.service;

import android.app.DownloadManager;

import com.android.volley.Request;
import com.example.apptive.gom.model.Generation;
import com.rainy.networkhelper.annotation.RequestMethod;
import com.rainy.networkhelper.request.ParserRequest;

/**
 * Created by apptive on 2017. 02. 08..
 */
@RequestMethod(method = Request.Method.POST, url = "http://192.168.1.101:8080/generation/next")
public class SendGenerationRequest extends ParserRequest<Generation>{

    public SendGenerationRequest(Generation generation) {
        setRequestDto(generation);
    }
}
