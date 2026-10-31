package ru.anton2319.vpnoverssh;

import android.os.Bundle;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import ru.anton2319.vpnoverssh.data.PayloadProfile;
import ru.anton2319.vpnoverssh.data.utils.PayloadProfileManager;

public class PayloadManagerActivity extends AppCompatActivity {
    private PayloadProfileManager manager;
    private LinearLayout list;

    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        manager = new PayloadProfileManager(this);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(20, 20, 20, 20);
        Button add = new Button(this);
        add.setText("Add Payload");
        root.addView(add);
        ScrollView scroll = new ScrollView(this);
        list = new LinearLayout(this);
        list.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(list);
        root.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));
        setContentView(root);
        add.setOnClickListener(v -> edit(null));
        refresh();
    }

    private void refresh() {
        list.removeAllViews();
        List<PayloadProfile> profiles = manager.loadProfiles();
        for (PayloadProfile p : profiles) {
            Button row = new Button(this);
            row.setAllCaps(false);
            row.setText(p.name + "  •  " + p.proxyHost + ":" + p.proxyPort);
            row.setOnClickListener(v -> new AlertDialog.Builder(this)
                    .setTitle(p.name)
                    .setItems(new String[]{"Edit", "Delete"}, (d, which) -> {
                        if (which == 0) edit(p);
                        else new AlertDialog.Builder(this).setMessage("Delete this payload?")
                                .setNegativeButton("Cancel", null)
                                .setPositiveButton("Delete", (dialog, w) -> {
                                    manager.deleteProfile(p.uuid);
                                    refresh();
                                }).show();
                    }).show());
            list.addView(row, new LinearLayout.LayoutParams(-1, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        if (profiles.isEmpty()) {
            android.widget.TextView empty = new android.widget.TextView(this);
            empty.setText("No saved payloads. Tap Add Payload to create one.");
            list.addView(empty);
        }
    }

    private void edit(PayloadProfile existing) {
        PayloadProfile p = existing == null ? new PayloadProfile() : existing;
        LinearLayout form = new LinearLayout(this);
        form.setOrientation(LinearLayout.VERTICAL);
        EditText name = field("Payload name", p.name, InputType.TYPE_CLASS_TEXT);
        EditText host = field("Proxy host", p.proxyHost, InputType.TYPE_TEXT_VARIATION_URI);
        EditText port = field("Proxy port", p.proxyPort == 0 ? "" : String.valueOf(p.proxyPort), InputType.TYPE_CLASS_NUMBER);
        EditText body = field("Payload text", p.payload, InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        body.setMinLines(4);
        form.addView(name); form.addView(host); form.addView(port); form.addView(body);
        new AlertDialog.Builder(this).setTitle(existing == null ? "New Payload" : "Edit Payload")
                .setView(form).setNegativeButton("Cancel", null)
                .setPositiveButton("Save", (d, w) -> {
                    String n=name.getText().toString().trim(), h=host.getText().toString().trim();
                    String ps=port.getText().toString().trim(), text=body.getText().toString();
                    int number;
                    try { number=Integer.parseInt(ps); } catch (Exception e) { number=-1; }
                    if (n.isEmpty() || h.isEmpty() || number < 1 || number > 65535 || text.trim().isEmpty()) {
                        Toast.makeText(this, "Enter name, proxy host, valid port and payload", Toast.LENGTH_LONG).show();
                        return;
                    }
                    p.name=n; p.proxyHost=h; p.proxyPort=number; p.payload=text;
                    if (manager.saveProfile(p)) refresh();
                    else Toast.makeText(this, "Could not save payload", Toast.LENGTH_LONG).show();
                }).show();
    }

    private EditText field(String hint, String value, int type) {
        EditText e = new EditText(this);
        e.setHint(hint); e.setInputType(type); e.setText(value);
        return e;
    }
}
