package ru.bmstu.tp.android_client.Services.Exceptions;

/*
    Session Id can be deleted on a server, if account isn't actives for a long time
     or GCM Id of this Session Id was cleaning by Google
 */
public class BadSessionIdException extends Exception {
}
