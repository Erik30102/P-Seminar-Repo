/*  Datei: src/main/java/com/Pseminar/Editor/EntityEditor.java  */
package com.Editor;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.List;

import com.Pseminar.ECS.Entity;
import com.Pseminar.ECS.Component;
import com.Pseminar.ECS.Transform;
import org.joml.Vector2f;

public class EntityEditor extends JFrame {
    private final Entity entity;
    private final JPanel fieldsPanel;

    /* ------------------------ Konstruktor ------------------------ */
    public EntityEditor(Entity entity) {
        this.entity = entity;

        setTitle("P-Seminar Entity-Editor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(550, 650);
        setLayout(new BorderLayout());

        fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));

        JButton refreshBtn = new JButton("🔄 Refresh");
        refreshBtn.addActionListener(e -> reloadFields());

        add(refreshBtn, BorderLayout.NORTH);
        add(new JScrollPane(fieldsPanel), BorderLayout.CENTER);
        add(componentControlPanel(), BorderLayout.SOUTH);

        reloadFields();
        setVisible(true);
    }

    /* ------------------------ UI aufbauen ------------------------ */
    private void reloadFields() {
        fieldsPanel.removeAll();
        buildTransformPanel(entity.transform);
        buildComponentPanels(entity.getComponents());
        fieldsPanel.revalidate();
        fieldsPanel.repaint();
    }

    /* -------- Transform separat (kein Component) -------- */
    private void buildTransformPanel(Transform t) {
        JPanel p = new JPanel(new GridLayout(0, 2));
        p.setBorder(BorderFactory.createTitledBorder("Transform"));

        Vector2f pos = t.GetPosition();
        JTextField px = new JTextField(String.valueOf(pos.x));
        JTextField py = new JTextField(String.valueOf(pos.y));

        Vector2f sc = t.GetScale();
        JTextField sx = new JTextField(String.valueOf(sc.x));
        JTextField sy = new JTextField(String.valueOf(sc.y));

        JTextField rot = new JTextField(String.valueOf(t.GetRotation()));

        p.add(new JLabel("Pos X")); p.add(px);
        p.add(new JLabel("Pos Y")); p.add(py);
        p.add(new JLabel("Scale X")); p.add(sx);
        p.add(new JLabel("Scale Y")); p.add(sy);
        p.add(new JLabel("Rotation")); p.add(rot);

        JButton apply = new JButton("Apply");
        apply.addActionListener(e -> {
            t.setPosition(Float.parseFloat(px.getText()), Float.parseFloat(py.getText()));
            t.setScale(Float.parseFloat(sx.getText()), Float.parseFloat(sy.getText()));
            t.setRotation(Float.parseFloat(rot.getText()));
        });

        fieldsPanel.add(p);
        fieldsPanel.add(apply);
    }

    /* -------- Panels für jede Component -------- */
    private void buildComponentPanels(List<Component> comps) {
        for (Component c : comps) {
            Class<?> cls = c.getClass();
            JPanel cp = new JPanel(new GridLayout(0, 2));
            cp.setBorder(BorderFactory.createTitledBorder(cls.getSimpleName()));

            for (Field f : cls.getDeclaredFields()) {
                f.setAccessible(true);
                Object val;
                try { val = f.get(c); } catch (IllegalAccessException e) { continue; }

                JLabel lbl = new JLabel(f.getName());
                JComponent editor = createEditor(f, c, val);

                cp.add(lbl);
                cp.add(editor);
            }
            fieldsPanel.add(cp);
        }
    }

    /* -------- Editor-Widget pro Field -------- */
    private JComponent createEditor(Field f, Component c, Object val) {
        Class<?> t = f.getType();

        if (isNumeric(t)) {                           // Textfeld + Slider-Popup
            JTextField tf = new JTextField(String.valueOf(val));
            addSliderPopup(tf, f, c);
            tf.addActionListener(e -> apply(f, c, tf.getText()));
            return tf;
        }
        if (t == boolean.class || t == Boolean.class) {
            JCheckBox cb = new JCheckBox("", val != null && (boolean) val);
            cb.addActionListener(e -> apply(f, c, String.valueOf(cb.isSelected())));
            return cb;
        }
        JTextField tf = new JTextField(val != null ? val.toString() : "");
        tf.addActionListener(e -> apply(f, c, tf.getText()));
        return tf;
    }

    /* -------- Slider-Support -------- */
    private void addSliderPopup(JTextField tf, Field f, Component c) {
        JPopupMenu m = new JPopupMenu();
        JMenuItem toSlider = new JMenuItem("Als Slider anzeigen");
        JMenuItem toText   = new JMenuItem("Als Textfeld anzeigen");
        m.add(toSlider); m.add(toText);
        tf.setComponentPopupMenu(m);

        toSlider.addActionListener(e -> {
            int init = (int) Float.parseFloat(tf.getText());
            JSlider sl = new JSlider(0, 100, init);
            sl.addChangeListener(ev -> {
                tf.setText(String.valueOf(sl.getValue()));
                apply(f, c, tf.getText());
            });
            replace(tf, sl);
        });
        toText.addActionListener(e -> replace(tf, tf));
    }

    /* -------- Utils -------- */
    private boolean isNumeric(Class<?> t) {
        return t == int.class || t == Integer.class || t == float.class || t == Float.class;
    }
    private void apply(Field f, Component c, String text) {
        try { f.set(c, convert(t(f), text)); } catch (Exception ignored) {}
    }
    private Class<?> t(Field f) { return f.getType(); }
    private Object convert(Class<?> t, String s) {
        if (t == int.class    || t == Integer.class)  return Integer.parseInt(s);
        if (t == float.class  || t == Float.class)    return Float.parseFloat(s);
        if (t == double.class || t == Double.class)   return Double.parseDouble(s);
        if (t == boolean.class|| t == Boolean.class)  return Boolean.parseBoolean(s);
        return s;
    }
    private void replace(JComponent oldC, JComponent newC) {
        Container p = oldC.getParent();
        int idx = Arrays.asList(p.getComponents()).indexOf(oldC);
        p.remove(oldC); p.add(newC, idx);
        p.revalidate(); p.repaint();
    }

    /* -------- Panel Add/Remove -------- */
    private JPanel componentControlPanel() {
        JPanel p = new JPanel(new FlowLayout());

        JTextField classTf = new JTextField(28);
        JButton add = new JButton("➕ Add");
        JButton rem = new JButton("➖ Remove");

        add.addActionListener(e -> {
            try {
                Class<?> cls = Class.forName(classTf.getText());
                if (Component.class.isAssignableFrom(cls)) {
                    Component c = (Component) cls.getDeclaredConstructor().newInstance();
                    c.setEntity(entity);
                    entity.AddComponent(c);
                    reloadFields();
                }
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        rem.addActionListener(e -> {
            try {
                Class<?> cls = Class.forName(classTf.getText());
                entity.getComponents().removeIf(c -> c.getClass().equals(cls));
                reloadFields();
            } catch (Exception ex) { ex.printStackTrace(); }
        });

        p.add(new JLabel("Component-Klasse:"));
        p.add(classTf);
        p.add(add);
        p.add(rem);
        return p;
    }

    /* -------- Test-Main -------- */
    public static void main(String[] args) {
        Entity e = new Entity();
        new EntityEditor(e);
    }
}