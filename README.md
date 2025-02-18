# **Smart Shopping Cart App**

### Revolutionizing the shopping experience using IoT technology

---

## **Overview**

The **Smart Shopping Cart App** is an innovative IoT-based solution designed to streamline the shopping process by allowing users to effortlessly scan products and manage their shopping cart through a mobile application. The app aims to reduce the need for traditional billing lines and enhance the overall shopping experience by integrating features like barcode scanning, real-time product validation, and load cell verification, ensuring that no additional products are added without scanning.

---

## **Features**

- **Product Scanning**: Scan products via a smartphone or a camera module attached to the cart (ESP32).
- **Product Verification**: The app connects to Firebase to fetch product details such as name, price, and weight.
- **Load Cell Integration**: Ensures no unscanned products are added to the cart by validating product weight.
- **Manual Product Entry**: Users can manually input product IDs if barcode scanning is not possible.
- **Real-time Cart Management**: View all scanned items, their prices, and the total cost in the app.
- **Seamless Payments**: Allows users to skip checkout lines by paying directly through the app.

---

## **Technologies Used**

- **Mobile App**: Android (Java/Kotlin)
- **Backend**: Firebase for real-time product validation
- **IoT**: ESP32 camera module for scanning barcodes
- **Load Cell**: Used to measure the weight of the cart's products
- **RecyclerView**: For displaying product details in the app interface

---

## **How It Works**

1. **Connect to Cart**: The user connects the app to the smart shopping cart via Bluetooth.
2. **Scan Products**: Products can be scanned using the smartphone’s camera or the ESP32 camera module attached to the cart.
3. **Fetch Product Details**: After scanning, the product ID is sent to Firebase, where the product’s name, price, and weight are retrieved.
4. **Load Cell Verification**: The load cell ensures that all products added to the cart are scanned. If an unscanned item is detected, the system alerts the user.
5. **Manual Entry**: In case the barcode cannot be scanned, the user can manually enter the product ID into the app.
6. **Checkout and Pay**: After completing the shopping, the user can pay the total amount directly through the app.

---

## **Installation Instructions**

1. Clone the repository:
    ```bash
    git clone https://github.com/vjymisal0/Smart-Shopping-Cart-IOT
    ```

2. Open the project in Android Studio.

3. Connect the app to your Firebase project:
   - Go to [Firebase Console](https://console.firebase.google.com/).
   - Create a new Firebase project.
   - Add your Android app to Firebase.
   - Download the `google-services.json` file and place it in the `app` directory of your project.

4. Set up Firebase Database:
   - Create a reference for products under `products` in Firebase.
   - Populate it with product IDs, names, prices, and weights.

5. Build and run the app on your Android device or emulator.

6. Make sure your ESP32 camera module is set up correctly and connected to the cart for barcode scanning.

---

## **Project Structure**

- **/app**: Android app source code
- **/firebase**: Firebase integration for real-time data
- **/esp32**: Code for ESP32 camera module

---

## **Screenshots**

<div style="display: flex; flex-wrap: wrap; justify-content: space-between;">

<img src="/screenshots/HomePage.jpg" alt="Home Screen" width="300" style="margin: 10px;"/>
<img src="/screenshots/ShoppingList.jpg" alt="Product List" width="300" style="margin: 10px;"/>
<img src="/screenshots/Products_added.jpg" alt="Product Details" width="300" style="margin: 10px;"/>
<img src="/screenshots/TwoModules.jpg" alt="Two Modules" width="300" style="margin: 10px;"/>
<img src="/screenshots/AddProductsManually.jpg" alt="Add Products Manually" width="300" style="margin: 10px;"/>
<img src="/screenshots/ActualCart.jpg" alt="Actual Cart" width="300" style="margin: 10px;"/>

</div>



## **Team Members**

- **Vedant Patil**
- **Vasundhara Gadakh**
- **Sudhir Bagad**
- **Mayuri Phad**
- **Vijay Misal**

---

---

## **Future Enhancements**

- **Payment Gateway Integration**: Seamless payment options directly through the app.
- **Multilingual Support**: Expanding the app’s accessibility by adding multiple languages.
- **AI Product Recommendations**: Suggesting products based on shopping history.
- **In-store Navigation**: Helping users locate items in the store with indoor navigation.

---

## **License**

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## **Contact**

For any questions or inquiries, feel free to reach out:

- Email: vijay.22320079@viit.ac.in
- LinkedIn: https://www.linkedin.com/in/vijaymisal/


