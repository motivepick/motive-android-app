# Motive Android App

## To Run This Application Locally

1. Make sure the server is running locally on `8080` port.
2. Run `ifconfig | grep inet` in a command line and find out your local IP address there. It should look something like `192.168.0.13`.
3. Modify `network_security_config.xml` and `application.properties` files to use your local IP address and `8080` port.
4. Run your application on a simulator by e.g. Android Studio.
