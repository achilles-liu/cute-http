## What is Cute-Http ?

As application programmers, we have to send a large number of http requests to debug our API everyday, so a primary objective should be to improve development efficiency by simplifying the process of sending http requests. Cute-Http is intended to make it more easier for sending http requests, otherwise, it just dependency on the JDK platform and don't need to install.

## How to package Cute-Http ?

Assuming you have installed JDK(>=1.8) and Maven(>=3.0) here.

- clone the source code

  ```
  https://github.com/achilles-liu/cute-http.git
  ```

- package the source code

  ```
  cd cute-http
  maven clean pacakge -Dmaven.test.skip=true
  ```

  The executable file(.exe) will be found in the target directory.

## How to use Cute-Http ?

### Feature Introduction

![Feature](https://github.com/achilles-liu/cute-http/blob/master/src/main/resources/png/cute-http-1.jpg)
#### Request Area
You can build your http request in Request Area.
#### Response Area
The area will show the response information.
#### Request History Area
Request History Area will record all history which you have sent out. You can double-click `Request` column to preview the detail of request.
### Usage
#### Build HTTP Request

![Request](https://github.com/achilles-liu/cute-http/blob/master/src/main/resources/png/build-request.jpg)

After building your http request, the `send` button will be clicked to send this request.
#### Extract HTTP Response
The response will be place in the `Respsonse Area`, and the the history will shown in the `Request History Area` as follows,

![](https://github.com/achilles-liu/cute-http/blob/master/src/main/resources/png/response-info.jpg)
### Summary
N/A
