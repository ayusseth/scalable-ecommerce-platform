import { useEffect, useState } from "react";

import MainLayout from "../../layouts/MainLayout";

import {
  getAddresses,
  deleteAddress,
  setDefaultAddress,
} from "../../services/addressService";

function AddressesPage() {
  const [addresses, setAddresses] =
    useState([]);

  const [loading, setLoading] =
    useState(true);

  const loadAddresses = async () => {
    try {
      const data =
        await getAddresses();

      setAddresses(data);
    } catch (error) {
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadAddresses();
  }, []);

  const handleDelete = async (id) => {
    try {
      await deleteAddress(id);

      await loadAddresses();

      alert(
        "Address deleted successfully"
      );
    } catch (error) {
      console.error(error);
    }
  };

  const handleDefault = async (id) => {
    try {
      await setDefaultAddress(id);

      await loadAddresses();

      alert(
        "Default address updated"
      );
    } catch (error) {
      console.error(error);
    }
  };

  if (loading) {
    return (
      <MainLayout>
        <div className="p-10">
          Loading...
        </div>
      </MainLayout>
    );
  }

  return (
    <MainLayout>
      <div className="max-w-5xl mx-auto p-10">

        <h1 className="text-4xl font-bold mb-8">
          My Addresses
        </h1>

        {addresses.length === 0 ? (
          <p>
            No addresses found
          </p>
        ) : (
          <div className="space-y-4">

            {addresses.map(
              (address) => (
                <div
                  key={address.id}
                  className="
                    border
                    rounded-lg
                    p-5
                    shadow-sm
                  "
                >
                  <h3 className="font-bold">
                    {address.fullName}
                  </h3>

                  <p>
                    {address.phoneNumber}
                  </p>

                  <p>
                    {address.addressLine1}
                  </p>

                  {address.addressLine2 && (
                    <p>
                      {
                        address.addressLine2
                      }
                    </p>
                  )}

                  <p>
                    {address.city},{" "}
                    {address.state}
                  </p>

                  <p>
                    {address.country}
                  </p>

                  <p>
                    {address.postalCode}
                  </p>

                  {address.defaultAddress && (
                    <span
                      className="
                        inline-block
                        mt-2
                        bg-green-100
                        text-green-700
                        px-3
                        py-1
                        rounded
                        text-sm
                      "
                    >
                      Default Address
                    </span>
                  )}

                  <div className="flex gap-3 mt-4">

                    {!address.defaultAddress && (
                      <button
                        onClick={() =>
                          handleDefault(
                            address.id
                          )
                        }
                        className="
                          bg-blue-600
                          text-white
                          px-4
                          py-2
                          rounded
                        "
                      >
                        Make Default
                      </button>
                    )}

                    <button
                      onClick={() =>
                        handleDelete(
                          address.id
                        )
                      }
                      className="
                        bg-red-600
                        text-white
                        px-4
                        py-2
                        rounded
                      "
                    >
                      Delete
                    </button>

                  </div>
                </div>
              )
            )}

          </div>
        )}

      </div>
    </MainLayout>
  );
}

export default AddressesPage;