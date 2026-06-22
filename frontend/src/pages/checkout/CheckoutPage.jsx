/* global Razorpay */
import { useEffect, useState } from "react";

import { useCart } from "../../store/cartStore";
import { createOrder } from "../../services/orderService";

import {
  createRazorpayOrder,
  verifyPayment,
} from "../../services/paymentService";

import MainLayout from "../../layouts/MainLayout";

import { getAddresses } from "../../services/addressService";

function CheckoutPage() {
  const [addresses, setAddresses] = useState([]);

  const [loading, setLoading] = useState(true);

  const [selectedAddress, setSelectedAddress] = useState(null);

  const { cartItems } = useCart();

  const handleCreateOrder = async () => {
    try {
      if (!selectedAddress) {
        alert("Please select an address");
        return;
      }

      const orderRequest = {
        items: cartItems.map((item) => ({
          productId: item.product.id,
          quantity: item.quantity,
        })),
        addressId: selectedAddress,
      };

      const order = await createOrder(orderRequest);

      console.log("ORDER", order);

      const razorpayOrder = await createRazorpayOrder(order.orderId);

      console.log("RAZORPAY ORDER", razorpayOrder);

      const options = {
        key: razorpayOrder.keyId,

        amount: razorpayOrder.amount,

        currency: razorpayOrder.currency,

        order_id: razorpayOrder.razorpayOrderId,

        name: "Ayush Ecommerce",

        description: "Order Payment",

        handler: async function (response) {
          try {
            await verifyPayment({
              razorpay_order_id: response.razorpay_order_id,

              razorpay_payment_id: response.razorpay_payment_id,

              razorpay_signature: response.razorpay_signature,
            });

            alert("Payment Successful");
          } catch (error) {
            console.error(error);

            alert("Payment Verification Failed");
          }
        },

        theme: {
          color: "#2563eb",
        },
      };

      const razorpay = new window.Razorpay(options);

      razorpay.open();
    } catch (error) {
      console.error(error);

      alert("Checkout Failed");
    }
  };

  const loadAddresses = async () => {
    try {
      const data = await getAddresses();

      setAddresses(data);

      const defaultAddress = data.find((address) => address.defaultAddress);

      if (defaultAddress) {
        setSelectedAddress(defaultAddress.id);
      }
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadAddresses();
  }, []);

  if (loading) {
    return (
      <MainLayout>
        <div className="p-10">Loading...</div>
      </MainLayout>
    );
  }

  return (
    <MainLayout>
      <div className="max-w-5xl mx-auto p-10">
        <h1 className="text-4xl font-bold mb-8">Checkout</h1>

        <h2 className="text-2xl font-semibold mb-4">Select Delivery Address</h2>

        <div className="space-y-4">
          {addresses.map((address) => (
            <div
              key={address.id}
              className={`
                border
                rounded-lg
                p-5
                cursor-pointer
                ${
                  selectedAddress === address.id
                    ? "border-blue-600 bg-blue-50"
                    : ""
                }
              `}
              onClick={() => setSelectedAddress(address.id)}
            >
              <input
                type="radio"
                checked={selectedAddress === address.id}
                onChange={() => setSelectedAddress(address.id)}
              />

              <h3 className="font-bold mt-2">{address.fullName}</h3>

              <p>{address.phoneNumber}</p>

              <p>{address.addressLine1}</p>

              {address.addressLine2 && <p>{address.addressLine2}</p>}

              <p>
                {address.city}, {address.state}
              </p>

              <p>{address.country}</p>

              <p>{address.postalCode}</p>
            </div>
          ))}
        </div>
        <div className="mt-8">
          <button
            onClick={handleCreateOrder}
            className="
      bg-green-600
      text-white
      px-6
      py-3
      rounded
    "
          >
            Create Order
          </button>
        </div>
      </div>
    </MainLayout>
  );
}

export default CheckoutPage;
