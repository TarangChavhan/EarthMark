package com.example.earthmark.Common;
public class Urls {
    public static String webServiceAddress = "http://192.168.35.13:80/EarthMark/";
    public static String loginUserWebService= webServiceAddress+"login.php";
    public static String AllCategoryDetails = webServiceAddress+"getAllCategoryDetails.php";
    public static String RegisterUserWebService = webServiceAddress+"userregister.php";
    public static String UserForgetPasswordWebService = webServiceAddress+"userForgetPassword.php";
    public static String myDetailsWebservice = webServiceAddress+"myDetails.php";
    public static String getAllCategoryImages ="http://192.168.35.13:80/EarthMark/images/";
    public static String getAllCategoryWiseEvents =webServiceAddress+"categoryWiseEvent.php";
    public static String updateMyProfileWebService =webServiceAddress +"updateMyProfile.php";

    //Admin Side
    public static String getAllCustomerLocationWebService = webServiceAddress+"getAllCustomerLocation.php";
    public  static String getAllCustomerrDeatailswebservice = webServiceAddress+"getAllCustomerDetails.php";
    public static String deleteUserWebService = webServiceAddress+"deleteuser.php";
    public static String UploadData = webServiceAddress+"upload.php";

}