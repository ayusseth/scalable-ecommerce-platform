import { useEffect, useState } from "react";

import MainLayout from "../../layouts/MainLayout";

import { getAddresses } from "../../services/addressService";

function CheckoutPage() {
  const [addresses, setAddresses] = useState([]);

  const [loading, setLoading] = useState(true);

  const [selectedAddress, setSelectedAddress] = useState(null);

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
      </div>
    </MainLayout>
  );
}

export default CheckoutPage;
