package com.sandeveloper.jsscolab.domain.Constants

object Endpoints {

    const val ONLINE_MODE="Online"
    const val OFFLINE_MODE="Offline"
    const val USERS = "Users"
    const val APP_CONFIG = "AppConfig"

//    const val defaultOrganisation="None"
//    const val defaultProject="None"                //Oxidizer
//    const val defaultAlias="None"                  //OXR
//    const val defaultSegment="Select Segment"      //Getting started
//    val defaultSections=listOf("Must Read", "Features", "Working", "Instructions")
//    const val defaultLightSensi=.1F
//    const val defaultMediumSensi=1.5F
//    const val defaultHeavySensi=2.5F
//    const val List="List"
//    const val Board="Board"

    object SharedPref {
        const val SHAREDPREFERENCES = "JSSPREFPREF"
    }


    object CodeViewer {
        val LANG = "LANG"
        const val CODE = "CODE"

    }
//
//    object DynamicLink {
//        const val OxgnHost = "https://oxgn.page.link"
//
//    }
    object broadcategories{
        const val coshop = "Coshop"
        const val quantum_exchange = "Quantum Exchange"
        const val shared_cab = "Shared Cab"
        const val my_posts = "My Posts"
    }

    object categories{
        const val Pharmaceuticals = "Pharmaceuticals"
        const val QuickCommerce = "Quick Commerce"
        const val FoodDelivery = "Food Delivery"
        const val ECommerce = "E-Commerce"
        const val SharedCab = "Shared Cab"
        const val Exchange = "Exchange"
    }
    object Storage {
        const val DP_PATH = "/DP/dp"
        const val IMAGE_PATH = "/icon/img"


        ////Project Related\\\\
        //Project Logo
        const val PROJECTS="PROJECTS"
        const val LOGO="LOGO"

        //Summary related
        const val COMMONS="COMMONS"


        ////User Related\\\\
        const val USERS="USERS"
        const val DP="DP"




    }

    object TaskDetails{

        const val EMPTY_MODERATORS = "None"

    }

    object Updates{
        val update = "updates"
        val DOWNLOAD_SIZE: String = "DOWNLOAD_SIZE"
        val RELEASE_NOTES: String = "RELEASE_NOTES"
        val UPDATE_URL: String = "UPDATE_URL"
        val VERSION_CODE: String = "VERSION_CODE"
        val VERSION_NAME: String = "VERSION_NAME"
    }



    object User {

        val FCM_TOKEN = "FCM_TOKEN"
        val DP_URL = "DP_URL"
        const val PROJECTS = "PROJECTS"
        const val EMAIL = "EMAIL"
        const val USERNAME = "USERNAME"
        const val FULLNAME="FULLNAME"
        const val PHONE = "PHONE"
        const val ADDRESS = "ADDRESS"
        const val PHOTO_ADDED = "PHOTO_ADDED"
        const val ADMN_NUMBER = "ADMISSION_NUMBER"
        const val DETAILS_ADDED = "DETAILS_ADDED"
        const val EMAIL_VERIFIED="EMAIL_VERIFIED"
        const val NOTIFICATION_TIME_STAMP = "NOTIFICATION_LAST_SEEN"
        const val TIMESTAMP="TIMESTAMP"
        const val LAST_NOTIFICATION_UPDATED="LAST_NOTIFICATION_UPDATED"
        const val IS_BLOCKED = "IS_BLOCKED"
    }

    object Workspace {
        const val WORKSPACE = "WORKSPACE"
        const val STATUS = "status"
        const val ID = "id"

    }


    object postCreate{
            const val POST_ID = "_id"
            const val POST_CATEGORY = "category"
            const val POST_APPS = "postApps"
            const val POST_DESCRIPTION = "comment"
            const val POST_LOGO = "postLogo"
            const val TOTAL_AMOUNT = "total_required_amount"
            const val CREATORS_CONTRIBUTION = "sender_contribution"
            const val EXPIRATION_DATE = "expiration_date"
        object filter{
            const val MYYEAR = "my_year"
            const val ADDRESS = "filter_address"
            const val BRANCH = "branch"
        }

        //sender
        object Sender{
            const val ID = "sender_id"
            const val NAME = "full_name"
            const val ADMISSION_NUMBER = "admission_number"
            const val ADDRESS = "address"
            const val RATING = "rating"
            const val RATED_COUNT="rated_count"
            const val PHOTO ="secure_url"

        }

    }


    object Notifications {
        val TASKID: String = "TASKID"
        const val NOTIFICATION_LAST_SEEN = "NOTIFICATION_LAST_SEEN"
        const val NOTIFICATION_COUNT = "NOTIFICATION_COUNT"
        const val LATEST_NOTIFICATION_TIME_STAMP = "LATEST_NOTIFICATION_TIME_STAMP"
        const val NOTIFICATIONS = "NOTIFICATIONS"
        const val TIMESTAMP = "timeStamp"

        const val notificationID: String = "notificationID"
        const val notificationType: String = "notificationType"


        const val taskID: String = "taskID"
        const val title: String = "title"
        const val message: String = "message"
        const val channelId:String="channelID"
        const val fromUser: String = "fromUser"
        const val toUser: String = "toUser"
        const val timeStamp: String = "timeStamp"
        const val lastUpdated:String="lastUpdated"
        const val TO: String = "to"
        const val TITLE: String = "title"
        const val BODY: String = "body"
        const val DATA: String = "data"
        const val TYPE: String = "type"
        const val ADDRESS:String = "address"

        object Groups{
            const val COMMENT_NOTIF_GROUP: String = "Comment_Notification_Group"

        }

        object Types {
            const val REQUEST_FAILED_NOTIFICATION = 1
            const val UNKNOWN_TYPE_NOTIFICATION = -1

        }

    }


    object ROOM {

        object NOTIFICATIONS {
            const val NOTIFICATIONS_TABLE = "notifications"
            const val NOTIFICATIONS_DATABASE = "NOTIFICATION-DB"
        }
        object MESSAGES{
            const val USERLIST_TABLE="users_in_messages"
            const val USERLIST_DB="users_in_messages_db"
            const val MESSAGES_DB="message_db"
            const val MESSAGES_TABLE="message_table"
        }



    }


}