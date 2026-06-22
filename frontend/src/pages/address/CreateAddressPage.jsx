import { useState } from "react";
import { useNavigate } from "react-router-dom";

import MainLayout from "../../layouts/MainLayout";

import { createAddress } from "../../services/addressService";

function CreateAddressPage() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    fullName: "",
    phoneNumber: "",
    addressLine1: "",
    addressLine2: "",
    city: "",
    state: "",
    country: "",
    postalCode: "",
  });

  const [loading, setLoading] =
    useState(false);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]:
        e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      setLoading(true);

      await createAddress(formData);

      alert(
        "Address created successfully"
      );

      navigate("/addresses");
    } catch (error) {
      console.error(error);

      alert(
        "Failed to create address"
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <MainLayout>
      <div className="max-w-2xl mx-auto p-10">

        <h1 className="text-4xl font-bold mb-8">
          Add Address
        </h1>

        <form
          onSubmit={handleSubmit}
          className="space-y-4"
        >
          <input
            name="fullName"
            placeholder="Full Name"
            value={formData.fullName}
            onChange={handleChange}
            className="w-full border p-3 rounded"
          />

          <input
            name="phoneNumber"
            placeholder="Phone Number"
            value={formData.phoneNumber}
            onChange={handleChange}
            className="w-full border p-3 rounded"
          />

          <input
            name="addressLine1"
            placeholder="Address Line 1"
            value={formData.addressLine1}
            onChange={handleChange}
            className="w-full border p-3 rounded"
          />

          <input
            name="addressLine2"
            placeholder="Address Line 2"
            value={formData.addressLine2}
            onChange={handleChange}
            className="w-full border p-3 rounded"
          />

          <input
            name="city"
            placeholder="City"
            value={formData.city}
            onChange={handleChange}
            className="w-full border p-3 rounded"
          />

          <input
            name="state"
            placeholder="State"
            value={formData.state}
            onChange={handleChange}
            className="w-full border p-3 rounded"
          />

          <input
            name="country"
            placeholder="Country"
            value={formData.country}
            onChange={handleChange}
            className="w-full border p-3 rounded"
          />

          <input
            name="postalCode"
            placeholder="Postal Code"
            value={formData.postalCode}
            onChange={handleChange}
            className="w-full border p-3 rounded"
          />

          <button
            type="submit"
            disabled={loading}
            className="
              w-full
              bg-blue-600
              text-white
              p-3
              rounded
            "
          >
            {loading
              ? "Saving..."
              : "Save Address"}
          </button>
        </form>
      </div>
    </MainLayout>
  );
}

export default CreateAddressPage;