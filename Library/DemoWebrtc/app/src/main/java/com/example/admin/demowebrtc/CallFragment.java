package com.example.admin.demowebrtc;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.webrtc.RendererCommon;

/**
 * Created by Admin on 7/20/2016.
 */
public class CallFragment extends Fragment {

    private View controlView;
    private TextView contactView;
    private ImageButton disconnectButton;
    private ImageButton cameraSwitchButton;
    private ImageButton videoScalingButton;
    private OnCallEvents callEvents;
    private RendererCommon.ScalingType scalingType;
    private boolean videoCallEnabled = true;

    public interface OnCallEvents {
        public void onCallHangUp();
        public void onCameraSwitch();
        public void onVideoScalingSwitch(RendererCommon.ScalingType scalingType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        controlView =
                inflater.inflate(R.layout.fragment_call, container, false);

        // Create UI controls.
        contactView =
                (TextView) controlView.findViewById(R.id.contact_name_call);
        disconnectButton =
                (ImageButton) controlView.findViewById(R.id.button_call_disconnect);
        cameraSwitchButton =
                (ImageButton) controlView.findViewById(R.id.button_call_switch_camera);
        videoScalingButton =
                (ImageButton) controlView.findViewById(R.id.button_call_scaling_mode);

        // Add buttons click events.
        disconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callEvents.onCallHangUp();
            }
        });

        cameraSwitchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callEvents.onCameraSwitch();
            }
        });

        videoScalingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (scalingType == RendererCommon.ScalingType.SCALE_ASPECT_FILL) {
                    videoScalingButton.setBackgroundResource(
                            R.drawable.ic_action_full_screen);
                    scalingType = RendererCommon.ScalingType.SCALE_ASPECT_FIT;
                } else {
                    videoScalingButton.setBackgroundResource(
                            R.drawable.ic_action_return_from_full_screen);
                    scalingType = RendererCommon.ScalingType.SCALE_ASPECT_FILL;
                }
                callEvents.onVideoScalingSwitch(scalingType);
            }
        });
        scalingType = RendererCommon.ScalingType.SCALE_ASPECT_FILL;

        return controlView;
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle args = getArguments();
        if (args != null) {
            String contactName = args.getString(Constants.EXTRA_ROOMID);
            contactView.setText(contactName + " call");
            videoCallEnabled = args.getBoolean(Constants.EXTRA_VIDEO_CALL, true);
        }
        if (!videoCallEnabled) {
            cameraSwitchButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callEvents = (OnCallEvents) activity;
    }

}
