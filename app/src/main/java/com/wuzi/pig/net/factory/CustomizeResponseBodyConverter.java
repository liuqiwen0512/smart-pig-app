/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wuzi.pig.net.factory;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.wuzi.pig.entity.PigFarmEntity;
import com.wuzi.pig.entity.PigFarmListEntity;
import com.wuzi.pig.entity.ResponseEntity;
import com.wuzi.pig.net.entity.ResponsePigFarmEntity;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * 自定义接口数据集中处理类
 */
final class CustomizeResponseBodyConverter<T> implements Converter<ResponseBody, Object> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;
    private final Annotation[] mAnnotations;

    CustomizeResponseBodyConverter(Gson gson, TypeAdapter<T> adapter, Annotation[] annotations) {
        this.gson = gson;
        this.adapter = adapter;
        this.mAnnotations = annotations;
    }

    @Override
    public Object convert(ResponseBody value) throws IOException {
        JsonReader jsonReader = gson.newJsonReader(value.charStream());
        try {
            T responseObject = adapter.read(jsonReader);
            if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw new JsonIOException("JSON document was not fully consumed.");
            }
            if (responseObject == null) {
                throw new ResponseException(ResponseException.ERROR.RESULT_CODE_201);
            }
            /*if (responseObject instanceof ResponsePigFarmEntity) {
                return handleResponsePigFarmEntity((ResponsePigFarmEntity) responseObject);
            }*/

            return handleResponseEntity((ResponseEntity) responseObject);
        } finally {
            value.close();
        }
    }

    private Object handleResponseEntity(ResponseEntity entity) {
        // resCode 做统一处理
        int resCode = entity.getCode();
        // 提示语
        String resMsg = entity.getMsg();
        // TODO: response错误待处理
        Object data = entity.getData();
        if (ResponseException.ERROR.RESULT_CODE_200 == resCode) {
            if (data == null) {
                if (isNullable()) {
                    data = new Object();
                } else {
                    throw new ResponseException(ResponseException.ERROR.RESULT_CODE_201);//空数据
                }
            }
        } else {
            ResponseException customizeException = new ResponseException(resCode);
            customizeException.setMessage(resMsg);
            throw customizeException;
        }
        return data;
    }

    private Object handleResponsePigFarmEntity(ResponsePigFarmEntity entity) {
        // resCode 做统一处理
        int resCode = entity.getCode();
        // 提示语
        String resMsg = entity.getMsg();
        if (ResponseException.ERROR.RESULT_CODE_200 == resCode) {
            int total = entity.getTotal();
            List<PigFarmEntity> rows = entity.getRows();
            PigFarmListEntity pigFarmListEntity = new PigFarmListEntity();
            pigFarmListEntity.setRows(rows);
            pigFarmListEntity.setTotal(total);
            return pigFarmListEntity;
        }
        ResponseException customizeException = new ResponseException(resCode);
        customizeException.setMessage(resMsg);
        throw customizeException;
    }

    private boolean isNullable() {
        if (mAnnotations != null) {
            for (Annotation item: mAnnotations) {
                if (item instanceof ResponseNull) {
                    return true;
                }
            }
        }
        return false;
    }
}
