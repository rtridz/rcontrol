import Routes.*;
import com.sun.istack.internal.Nullable;

import java.util.HashMap;

/**
 * Created by vadim on 26.09.15.
 */
public class Router {
    private HashMap<String, String> routes; // url, handlerClassName

    public Router() {
        routes = new HashMap<>();
        // TODO Hard Code
        addRoute("/category", Category.class.getName());
        addRoute("/vendor", Vendor.class.getName());
        addRoute("/model", Model.class.getName());
        addRoute("/dump", Dump.class.getName());
    }

    @Nullable
    public Handler getHandler(String uri) {
        String handlerClassName = routes.get(uri);
        Handler handler = null;
        if (handlerClassName != null) {
            try {
                handler = (Handler) Class.forName(handlerClassName).newInstance();
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return handler;
    }

    public void addRoute(String url, String handlerClassName) {
        routes.put(url, handlerClassName);
    }
}