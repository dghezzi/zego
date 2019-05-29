/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.util;

/**
 *
 * @author Lu
 */
public class ZegoK {
    
    public static class Tuning
    {
        public static final Boolean TEST = Boolean.TRUE;
        public static final Boolean LOGGING = Boolean.TRUE;
        public static final Integer THROTTLING_SMS_THRESHOLD = 5;
    }
    
    public static class Error {
        public static final Integer USER_NOT_FOUND = 100;
        public static final Integer DEVICE_BLACKLISTED = 101;
        public static final Integer DUPLICATE_USERNAME = 102;
        public static final Integer PIN_INVALID = 103;
        public static final Integer REFERRALCODE_INVALID = 104;
        public static final Integer APP_OUTDATED = 105;
        public static final Integer USER_BANNED = 106;
        public static final Integer REFERRALCODE_BANNED = 107;
        public static final Integer TOO_MANY_SMS = 108;
        public static final Integer USER_FB_NOT_FOUND = 109;
        public static final Integer DUPLICATE_EMAIL = 110;
        public static final Integer DUPLICATE_FACEBOOKID = 111;
        public static final Integer STRIPE_CREATION_FAILED = 112;
        public static final Integer STRIPE_CARD_ALREADY_EXIST = 113;
        public static final Integer IMPOSSIBLE_TO_UPDATE_USER = 114;
        public static final Integer IMPOSSIBLE_TO_SEND_SMS = 115;
        public static final Integer STRIPE_CARD_ERROR = 116;
        public static final Integer STRIPE_PROCESSING_ERROT = 117;
        public static final Integer NEXMO_FAILURE = 118;
        public static final Integer USER_SHOULD_REVALIDATE_MOBILE = 119;
        public static final Integer NO_ZEGO_HERE = 120;
        public static final Integer RIDE_IS_TOO_SHORT = 121;        
        public static final Integer CANNOT_FIND_ROUTE  = 122;
        public static final Integer USER_IS_NOT_IDLE = 123;
        public static final Integer NO_DRIVERS = 124;
        public static final Integer UNKNOWN_ACTION = 125;
        public static final Integer USER_CANNOT_ACT_ON_THIS_RIDE = 126;
        public static final Integer DRIVER_CANNOT_ACCEPT_ACCEPTED_BY_OTHERS = 127;
        public static final Integer DRIVER_CANNOT_ACCEPT_CANCELED = 128;
        public static final Integer REQUEST_NOT_FOUND = 129;
        public static final Integer USER_CANNOT_CANCEL_A_RIDE = 130;
        public static final Integer DRIVER_CANNOT_ABORT_A_RIDE = 131;
        public static final Integer DRIVER_CANNOT_START_A_RIDE = 132;
        public static final Integer DRIVER_CANNOT_END_A_RIDE = 133;
        public static final Integer CANNOT_CREATE_FEEDBACK = 134;
        public static final Integer MISSING_PAYMENT_METHOD = 135;
        public static final Integer GOOGLE_API_FAILURE = 136;
        public static final Integer RIDE_IS_TOO_LONG = 137;
        public static final Integer USER_CANNOT_TERMINATE_A_RIDE = 138;
        public static final Integer USER_TOO_FAR_FROM_PICKUP = 139;
        public static final Integer MODE_NOT_SUPPORTED = 140;
        public static final Integer MODE_CHANGE_NOT_ALLOWED_NOW = 141;
        public static final Integer USER_CANNOT_DRIVE_YET = 142;
        public static final Integer PROMO_WRONG_DATES = 143;
        public static final Integer PROMO_WRONG_VALUE = 144;
        public static final Integer PROMO_CODE_ALREADY_EXIST = 145;
        public static final Integer PROMO_DOESNT_EXIST = 146;
        public static final Integer PROMO_CANNOT_BE_REEDEMED_NOW = 147;
        public static final Integer PROMO_IS_EXPIRED = 148;
        public static final Integer PROMO_IS_OVERUSED = 149;
        public static final Integer PROMO_ALREADY_USED_BY_THIS_USER = 150;
        public static final Integer MISSING_FUNDS = 151;
        public static final Integer PAYMENT_ACTION_NOT_ALLOWED = 152;
        public static final Integer TOO_MANY_CARD_ERROR = 153;
        public static final Integer PROMO_IS_FIRST_USE_ONLY = 154;
        public static final Integer REQUEST_IS_MISCONFIGURED = 155;
        public static final Integer USER_HAS_UNPAID_RIDES = 156;
        public static final Integer WRONG_CASHREQ_FORMAT = 157;
        
        public static final Integer AUTHFAIL = 403;

        public static final Integer TESTERROR = 900;
        public static final Integer DEFAULT_ERROR = 999;
        public static final Integer MISSING_MANDATORY_FIELDS = 998;
    }
    
    public static class Auth {
        public static final String ACCESSTOKEN = "zegotoken";
        public static final String SKIPTOKEN = "skiptoken";
        public static final String SKIPTOKENVALUE = "48ffafc5433e8c3eba96eb18fd3c4eb0";
    }
    
    public static class GCM 
    {
        public static final String APIKEY = "AIzaSyA17-RHgRdM37x02FpqDvOaLIYWSEaN6_o";
        public static final String PACKAGE = "it.sharethecity.mobile.letzgo";
        public static final Integer PUSH_TTL = 2*60; // 2 min in seconds       
    }
    
}
