package com.jasonette.seed.Component;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.jasonette.seed.Core.JasonViewActivity;
import com.jasonette.seed.temiconversation.TemiSpeakListener;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

public class JasonComponentFactory {
    Map<String, Integer> signature_to_type = new HashMap<String,Integer>();
    static WeakReference<TemiSpeakListener> speakListener = new WeakReference<>(null);
    public static View build(View prototype, final JSONObject component, final JSONObject parent, final Context context) {
        try{
            String type;
            type = component.getString("type");

            View view;
            if(type.equalsIgnoreCase("label")){
                view = JasonLabelComponent.build(prototype, component, parent, context);
            } else if(type.equalsIgnoreCase("image")) {
                view = JasonImageComponent.build(prototype, component, parent, context);
            } else if(type.equalsIgnoreCase("button")) {
                view = JasonButtonComponent.build(prototype, component, parent, context);
            } else if(type.equalsIgnoreCase("space")) {
                view = JasonSpaceComponent.build(prototype, component, parent, context);
            } else if(type.equalsIgnoreCase("textfield")) {
                view = JasonTextfieldComponent.build(prototype, component, parent, context);
            } else if(type.equalsIgnoreCase("textarea")) {
                view = JasonTextareaComponent.build(prototype, component, parent, context);
            } else if(type.equalsIgnoreCase("html")) {
                view = JasonHtmlComponent.build(prototype, component, parent, context);
            } else if(type.equalsIgnoreCase("map")) {
                view = JasonMapComponent.build(prototype, component, parent, context);
            } else if(type.equalsIgnoreCase("slider")) {
                view = JasonSliderComponent.build(prototype, component, parent, context);
            } else if(type.equalsIgnoreCase("switch")) {
                view = JasonSwitchComponent.build(prototype, component, parent, context);
            } else if(type.equalsIgnoreCase("speak")) {
                JSONObject customComponent = new JSONObject();
                customComponent.put("type", "label");
                customComponent.put("class", "bold");
                customComponent.put("text", "Tutorial");
                view = JasonLabelComponent.build(null, customComponent, null, context);
                view.setVisibility(View.GONE);
                Timber.d(component.getString("text"));
                speakListener.get().onSpeakTextFound(component.getString("text"));
            } else {
                // Non-existent component warning
                JSONObject error_component = new JSONObject(component.toString());
                error_component.put("type", "label");
                error_component.put("text", "$"+component.getString("type")+"\n(not implemented yet)");
                view = JasonLabelComponent.build(prototype, error_component, parent, context);
                ((TextView)view).setGravity(Gravity.CENTER);
            }

            // Focus textfield/textarea
            if (component.has("focus")) {
                ((JasonViewActivity)context).focusView = view;
            }

            return view;

        }
        catch (Exception e){
            Log.d("Warning", e.getStackTrace()[0].getMethodName() + " : " + e.toString());
        }

        return new View(context);
    }
    public static void addListener(TemiSpeakListener listener){
        speakListener = new  WeakReference<>(listener);
    }
}
