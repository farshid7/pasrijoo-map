package ir.waspar.farshid.map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.tileprovider.util.SimpleInvalidationHandler;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.TilesOverlay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FarshidMapView extends RelativeLayout implements LocationManeger.OnLocationReceive {
    private Context context;
    private MapView mapView;
    private TilesOverlay hibridTileOverlay = null;
    public MapTileProviderBasic mapTileProviderBasicParsijoo;
    public ItemizedIconOverlay<OverlayItem> itemizedIconOverlay;
    private String api_key;
    private View view;
    private View ZoomView;
    private ImageButton imageButton_Location;

    private boolean IsLocationEnabled=false;
    private Marker markerMyLocation;
    private boolean firstLocationLoad=false;
    private CallBackAddress callBackAddress;
    private CallBackSearch callBackSearch;
    private CallBackDirection callBackDirection;

    public FarshidMapView(Context context) {
        super(context);
        this.context = context;
        initializer(context, null, 0);
    }

    public FarshidMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initializer(context, attrs, 0);
    }

    public FarshidMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initializer(context, attrs, defStyleAttr);
    }

    private void initializer(final Context context, AttributeSet attrs, int defStyleAttr) {
        this.context = context;

        new PermissionHandler(context, new PermissionHandler.OnPermissionHandler() {
            @Override
            public void Handled() {

                Intent intent=new Intent();
                intent.setClass(context, ((Activity)context).getClass());
                context.startActivity(intent);
                ((Activity)context).finish();



            }
        });
        view = inflate(context, R.layout.viewer, this);
        mapView = view.findViewById(R.id.mapviewosm);
        imageButton_Location=view.findViewById(R.id.corrent_location);
        ZoomView=view.findViewById(R.id.zoom_view);
        imageButton_Location.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCorrentLocation();
            }
        });
        view.findViewById(R.id.map_zoom_in).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomIn();
            }
        });
        view.findViewById(R.id.map_zoom_out).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomOut();
            }
        });
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomViewer, defStyleAttr, 0);
        this.api_key = a.getString(R.styleable.CustomViewer_api_key);
        mapView.setTilesScaledToDpi(true);
        Configuration.getInstance().setMapViewHardwareAccelerated(true);
        Configuration.getInstance().setUserAgentValue(context.getPackageName());
        setConfigZoom(false);
        mapView.getController().setCenter(new GeoPoint(31.8974, 54.3569));
        mapView.getController().setZoom(5);
        mapView.setMaxZoomLevel(20);
        mapView.setMinZoomLevel(3);
        setConfigMultiTouch(true);
        parsijooTileProvider();
        addPointLayer();
        setupLocationBotton();


    }

    public void setZoomLevel(int Level){
        mapView.getController().setZoom(Level);

    }
    public void setApi_key(String api_key){
        this.api_key=api_key;
    }
    private void setupLocationBotton() {
        if(!IsLocationEnabled){

            imageButton_Location.setVisibility(GONE);
        }else {
            imageButton_Location.setVisibility(VISIBLE);
            new LocationManeger(context,this);

        }
    }
    @Override
    public void onReceive(Double X, Double Y) {
        Log.e("location", "onReceive: "+X+" "+Y );
        if(markerMyLocation!=null){
            markerMyLocation.remove(mapView);
        }
//        mapView.getController().setCenter(new GeoPoint(X, Y));
//        mapView.getController().setZoom(14);
        if(!firstLocationLoad) {
            markerMyLocation = addMarker(Y, X, "موقعیت من", context.getResources().getDrawable(R.drawable.ic_point));
            firstLocationLoad=true;
        }else {
            markerMyLocation = addMarkerwithoutmove(Y, X, "موقعیت من", context.getResources().getDrawable(R.drawable.ic_point));

        }


    }
    public void setLocationEnabled(boolean isEnabled){
        this.IsLocationEnabled=isEnabled;
        setupLocationBotton();
    }    public void setZoomEnabled(boolean isEnabled){
        if(isEnabled){
            ZoomView.setVisibility(VISIBLE);
        }else {
            ZoomView.setVisibility(GONE);
        }
    }
    public MapView getMapView() {
        return mapView;
    }
    public void setConfigZoom(Boolean bool) {
        this.mapView.setBuiltInZoomControls(bool);
    }
    public void setConfigMultiTouch(Boolean bool) {
        this.mapView.setMultiTouchControls(bool);
    }
    public void parsijooHibridTileOverllay() {
        final MapTileProviderBasic tileProvider = new MapTileProviderBasic(context);
        ITileSource tileSource = new XYTileSource("Hibrid", 3, 17, 256, "",
                new String[]{
                        context.getString(R.string.base_tile_url_a) + "GetHibrid?imageID=p_",
                        context.getString(R.string.base_tile_url_b) + "GetHibrid?imageID=p_",
                        context.getString(R.string.base_tile_url_c) + "GetHibrid?imageID=p_",
                        context.getString(R.string.base_tile_url_d) + "GetHibrid?imageID=p_",
                        context.getString(R.string.base_tile_url_e) + "GetHibrid?imageID=p_",
                }) {
            @Override
            public String getTileURLString(MapTile aTile) {
                return getBaseUrl() + aTile.getX() + "_" + aTile.getY() + "_" + aTile.getZoomLevel();
            }
        };
        tileProvider.setTileSource(tileSource);
        hibridTileOverlay = new TilesOverlay(tileProvider, context);
        hibridTileOverlay.setLoadingBackgroundColor(Color.TRANSPARENT);
        mapView.getOverlays().add(hibridTileOverlay);
        mapView.invalidate();
    }
    public void parsijooTileProvider() {
        mapTileProviderBasicParsijoo = new MapTileProviderBasic(context);
        mapTileProviderBasicParsijoo.setTileRequestCompleteHandler(new SimpleInvalidationHandler(mapView));
        Configuration.getInstance().getAdditionalHttpRequestProperties().put("api-key", api_key);
        Configuration.getInstance().setDebugTileProviders(true);
        OnlineTileSourceBase parsijoo = new OnlineTileSourceBase("Road",3,19, 256, "",
                new String[]{
                        "http://developers.parsijoo.ir/web-service/v1/map/?type=tile"
                }) {
            @Override
            public String getTileURLString(MapTile aTile) {
                //System.out.println("sysosout " +getBaseUrl() + "&x=" + aTile.getX() + "&y=" + aTile.getY() + "&z=" + aTile.getZoomLevel()+"&api-key="+api_key);
                return getBaseUrl() + "&x=" + aTile.getX() + "&y=" + aTile.getY() + "&z=" + aTile.getZoomLevel();
            }
        };
        mapTileProviderBasicParsijoo.setTileSource(parsijoo);
        mapView.setTileProvider(mapTileProviderBasicParsijoo);
    }
    public Marker addMarker(double x,double y, String Title, Drawable drawable) {
        Marker marker = new Marker(mapView);
        GeoPoint geoPoint = new GeoPoint( y,x);
        marker.setPosition(geoPoint);
        if(Title!=null){
            marker.setTitle(Title);
        }
        if (drawable == null) {
            Drawable dr = context.getResources().getDrawable(R.drawable.ic_placeholder);
            marker.setIcon(dr);
        } else {
            marker.setIcon(drawable);
        }

        mapView.getController().animateTo(geoPoint);
        return addMarker(marker);
    }
    private Marker addMarkerwithoutmove(double x,double y, String Title, Drawable drawable) {
        Marker marker = new Marker(mapView);
        GeoPoint geoPoint = new GeoPoint( y,x);
        marker.setPosition(geoPoint);
        if(Title!=null){
            marker.setTitle(Title);
        }
        if (drawable == null) {
            Drawable dr = context.getResources().getDrawable(R.drawable.ic_placeholder);
            marker.setIcon(dr);
        } else {
            marker.setIcon(drawable);
        }

        return addMarker(marker);
    }
    public Marker addStaticMarker( double x,double y, String Title, Drawable drawable,int ZoomLevel) {
        Marker marker = new Marker(mapView);
        GeoPoint geoPoint = new GeoPoint(x ,y);
        marker.setPosition(geoPoint);
        if(Title!=null){
            marker.setTitle(Title);
        }
        if (drawable == null) {
            Drawable dr = context.getResources().getDrawable(R.drawable.ic_placeholder);
            marker.setIcon(dr);
        } else {
            marker.setIcon(drawable);
        }

        mapView.getController().animateTo(geoPoint);
        mapView.getController().setZoom(ZoomLevel);
        setLocationEnabled(false);
        setZoomEnabled(false);
        view.findViewById(R.id.cover).setVisibility(VISIBLE);

        return addMarker(marker);
    }

    public void goToCorrentLocation(){
        if(markerMyLocation!=null) {
            mapView.getController().animateTo(new GeoPoint(markerMyLocation.getPosition()));
        }
    }
    public void zoomIn(){
        mapView.getController().zoomIn();

    }
    public void zoomOut(){
        mapView.getController().zoomOut();

    }

    public Marker addMarker(Marker marker) {
        marker.setAnchor(Marker.ANCHOR_CENTER, 1.0f);
        mapView.getOverlays().add(marker);
        return marker;
    }
    public void addOnLongClickMap(final OnLongClickMap onLongClickMap){
        MapEventsReceiver mReceive = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {

                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                onLongClickMap.onClick(p.getLongitude(),p.getLatitude());
                return false;
            }
        };

        MapEventsOverlay OverlayEvents = new MapEventsOverlay(context, mReceive);
        mapView.getOverlays().add(OverlayEvents);

    }
    public void addOnClickMap(final OnClickMap onClickMap){
        MapEventsReceiver mReceive = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                onClickMap.onClick(p.getLongitude(),p.getLatitude());

                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };

        MapEventsOverlay OverlayEvents = new MapEventsOverlay(context, mReceive);
        mapView.getOverlays().add(OverlayEvents);

    }
    public interface OnLongClickMap{
        void onClick(Double X,Double Y);
    }
    public interface OnClickMap{
        void onClick(Double X,Double Y);
    }
    public void removeMarker(Marker marker) {
        mapView.getOverlays().remove(marker);
        mapView.invalidate();
    }
    private void addPointLayer() {
        ArrayList<OverlayItem> overlayArray = new ArrayList<>();
        itemizedIconOverlay = new ItemizedIconOverlay<>(context, overlayArray, null);
        mapView.getOverlays().add(itemizedIconOverlay);
    }
    public void clearAllMarkers() {
        itemizedIconOverlay.removeAllItems();
    }

    public void getAddress(double x, double y, final OnAddressReceive onAddressReceive) {
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, "https://developers.parsijoo.ir/web-service/v1/map/?type=address&x="+String.valueOf(x)+"&y="+String.valueOf(y), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject reqStatus=response.getJSONObject("reqStatus");

                    if(reqStatus.getInt("statusCode")!=200){
                        onAddressReceive.onNotFound();
                    }else {
                        ir.farshid.waspar.www.jsonparserlabrary.JsonParser jsonParser=new ir.farshid.waspar.www.jsonparserlabrary.JsonParser(Address.class);
                        onAddressReceive.onReceive((Address) jsonParser.ParsJson(response.getJSONObject("result")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    onAddressReceive.onError();                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onAddressReceive.onError();
            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("api-key", api_key);
                return headers;
            }
        };

        Volley.newRequestQueue(context).add(jsonObjectRequest);

    }
    public interface OnAddressReceive {
        void onReceive(Address address);
        void onNotFound();
        void onError();
    }
    public interface CallBackAddress {
        void onResponse(HashMap<String, String> resultAddress);

        void onError(NetworkResponse networkResponse);
    }
    public void getSearch(String q, final CallBackSearch callBackSearch) {
        doSearch(q, null, 0, 0, callBackSearch);
    }
    public void getSearch(String q, String city, int page, final CallBackSearch callBackSearch) {
        doSearch(q, city, page, 0, callBackSearch);
    }
    public void getSearch(String q, String city, int page, int nrpp, final CallBackSearch callBackSearch) {
        doSearch(q, city, page, nrpp, callBackSearch);
    }
    public void doSearch(String q, String city, int page, int nrpp, final CallBackSearch callBackSearch) {
        this.callBackSearch = callBackSearch;
        String query = "";
        try {
            query = URLEncoder.encode(q, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url = "http://developers.parsijoo.ir/web-service/v1/map/?type=search&q=" + query;
        url = (city != null) ? url + "&city=" + city : url;
        url = (page > 0) ? url + "&page=" + page : url;
        url = (nrpp > 0) ? url + "&nrpp=" + nrpp : url;
        System.out.println("sysosout url " + url);
        RequestQueue queue = Volley.newRequestQueue(context);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonParser parser = new JsonParser();
                        JsonObject rootObj = parser.parse(response).getAsJsonObject();
                        JsonObject resultObj = rootObj.getAsJsonObject("result");
                        int resultNumber = resultObj.get("resultNumber").getAsInt();
                        JsonArray itemsJsonArray = resultObj.getAsJsonArray("items");
                        ArrayList<HashMap<String, String>> itemsArray = new ArrayList<HashMap<String, String>>();
                        for (int i = itemsJsonArray.size() - 1; i >= 0; i--) {
                            JsonElement itemJsonElement = itemsJsonArray.get(i);
                            JsonObject itemJsonObject = itemJsonElement.getAsJsonObject();
                            HashMap<String, String> result = new HashMap<>();
                            result.put("title", itemJsonObject.get("title").getAsString());
                            result.put("longitude", itemJsonObject.get("longitude").getAsString());
                            result.put("latitude", itemJsonObject.get("latitude").getAsString());
                            result.put("zoom", itemJsonObject.get("zoom").getAsString());
                            result.put("type", itemJsonObject.get("type").getAsString());
                            result.put("name", itemJsonObject.get("name").getAsString());
                            result.put("address", itemJsonObject.get("address").getAsString());
                            itemsArray.add(result);
                        }
                        callBackSearch.onResponse(resultNumber, itemsArray);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error == null || error.networkResponse == null) {
                    return;
                }
                callBackSearch.onError(error.networkResponse);
                String body;
                final String statusCode = String.valueOf(error.networkResponse.statusCode);
                try {
                    body = new String(error.networkResponse.data, "UTF-8");
                    System.out.println("sysosout " + body);
                } catch (UnsupportedEncodingException e) {
                    // exception
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("api-key", api_key);
                return params;
            }
        };
        queue.add(stringRequest);
    }
    public interface CallBackSearch {
        void onResponse(int resultNumber, ArrayList<HashMap<String, String>> arrayListItems);
        void onError(NetworkResponse networkResponse);

    }
    public void getDirection(double lat1, double lon1, double lat2, double lon2, List<HashMap<String, Double>> middle_points, final CallBackDirection callBackDirection) {

        this.callBackDirection = callBackDirection;
        String p;
        Map params = new HashMap<>();
        params.put("lat1", lat1 + "");
        params.put("lon1", lon1 + "");
        params.put("lat2", lat2 + "");
        params.put("lon2", lon2 + "");
        List<Map> temp_middle_points_array = new ArrayList<>();
        for (int i = 0; i < middle_points.size(); i++) {
            Map temp_middle_points = new HashMap<>();
            temp_middle_points.put("lat", middle_points.get(i).get("lat") + "");
            temp_middle_points.put("lon", middle_points.get(i).get("lon") + "");
            temp_middle_points_array.add(temp_middle_points);
        }

        params.put("points", temp_middle_points_array);
        Gson gson = new Gson();
        p = gson.toJson(params);
        try {
            p = URLEncoder.encode(p, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = "http://developers.parsijoo.ir/web-service/v1/map/?type=direction&p=" + p;
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("qqq", "onResponse: "+response);
                        String wkt = "";
                        float totalDistance = (float) 0.0;
                        int totalTime = 0;
                        ArrayList<HashMap<String, String>> instructionList = new ArrayList<>();
                        JsonParser parser = new JsonParser();
                        JsonObject rootObj = parser.parse(response).getAsJsonObject();
                        if (rootObj.getAsJsonArray("result").size() > 0) {
                            JsonObject resultObj = (JsonObject) rootObj.getAsJsonArray("result").get(0);
                            wkt = resultObj.get("wkt").getAsString();
                            totalDistance = resultObj.get("totalDistance").getAsFloat();
                            totalTime = resultObj.get("totalTime").getAsInt();
                            JsonArray instructionListArray =  resultObj.getAsJsonArray("instructionList");
                            for (int i = 0; i < instructionListArray.size(); i++) {
                                HashMap<String, String> instruction = new HashMap<>();
                                instruction.put("name", instructionListArray.get(i).getAsJsonObject().get("name").getAsString());
                                instruction.put("distance", instructionListArray.get(i).getAsJsonObject().get("distance").getAsString());
                                instruction.put("time", instructionListArray.get(i).getAsJsonObject().get("time").getAsString());
                                instruction.put("text", instructionListArray.get(i).getAsJsonObject().get("text").getAsString());
                                instructionList.add(instruction);
                            }
                        }
                        callBackDirection.onResponse(wkt, totalDistance, totalTime, instructionList);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("qqq", "onErrorResponse: "+error.toString());
                if (error == null || error.networkResponse == null) {
                    return;
                }
                callBackDirection.onError();
                String body;
                final String statusCode = String.valueOf(error.networkResponse.statusCode);
                try {
                    body = new String(error.networkResponse.data, "UTF-8");
                    System.out.println("sysosout " + body);
                } catch (UnsupportedEncodingException e) {
                    // exception
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("api-key", api_key);
                return params;
            }


        };
        queue.add(stringRequest);
    }


//    public Location getLocation() {
//        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//        if (locationManager != null) {
//            @SuppressLint("MissingPermission") Location lastKnownLocationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            if (lastKnownLocationGPS != null) {
//                return lastKnownLocationGPS;
//            } else {
//                @SuppressLint("MissingPermission") Location loc =  locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
//
//                return loc;
//            }
//        } else {
//            return null;
//        }
//    }



    public interface CallBackDirection {
        void onResponse(String wkt, float totalDistance, int totalTime, ArrayList<HashMap<String, String>> instructionList);

        void onError();
    }
}