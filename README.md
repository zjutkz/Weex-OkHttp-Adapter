#Weex-OkHttp-Adapter

A network adapter based on [OkHttp](https://github.com/square/okhttp) can be put into [Weex](https://github.com/alibaba/weex)



#Download

I'm going to push to maven A.S.A.P.



#Usage

You can put this adapter into Weex when you init it.

```java
WXSDKEngine.initialize(this,new InitConfig.Builder()
        .setImgAdapter(.......)
        .setHttpAdapter(new OkHttpApdater())
        .build());
```





#License

```
Copyright 2016 zjutkz

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
        You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```