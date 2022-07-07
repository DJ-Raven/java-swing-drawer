# java-swing-drawer

This drawer custom with animation and miglayout and show drawer on glassPane of jfram
### Method
Builder Method | Parameters | Default Values
----------------------- | ------------ | ---------------
header | Component | <i>optional</i>
addChild | List\<Component\> | <i>optional</i>
addFooter | List\<Component\> | <i>optional</i>
space | int | optional | <i>optional</i>
separator | int , Color | <i>optional</i>
background | Color | new Color(30, 30, 30)
drawerBackground | Color | Color.WHITE
backgroundTransparent | float (0f to 1f) | 0.5f
drawerWidth | int | 250
headerHeight | int | 150
duration | int | 500
resolution | int | 10
itemHeight | int | 45
closeOnPress | boolean | true
enableScroll | boolean | false
enableScrollUI | boolean | true
leftDrawer | boolean | true
itemAlignLeft | boolean | true
event | EventDrawer | <i>optional</i>
build | none | required

### Example Build Drawer with jfram
```java
drawer = Drawer.newDrawer(jfram)
        .background(new Color(90, 90, 90))
        .enableScroll(true)
        .header(new JLabel("Header"))
        .space(3)

        .addChild(new DrawerItem("User ").icon(new ImageIcon(getClass().getResource("/icon/user.png"))).build())
        .addChild(new DrawerItem("Contacts").icon(new ImageIcon(getClass().getResource("/icon/cont.png"))).build())

        .addFooter(new DrawerItem("Exit").icon(new ImageIcon(getClass().getResource("/icon/exit.png"))).build())

        .event(new EventDrawer() {
            @Override
            public void selected(int index, DrawerItem item) {
                if (drawer.isShow()) {
                    drawer.hide();
                }
                //  More code here
            }
        })
        .build();
```
### Example how to show and hide drawer
```java
if (drawer.isShow()) {
    drawer.hide();
} else {
    drawer.show();
}
```
### Screenshot
![2022-07-06_232302](https://user-images.githubusercontent.com/58245926/177802837-518bfd34-9af5-4c42-b13c-945dd5cf1c32.png)
