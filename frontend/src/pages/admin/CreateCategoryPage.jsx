import { useState } from "react";
import { useNavigate } from "react-router-dom";

import MainLayout from "../../layouts/MainLayout";

import { createCategory } from "../../services/adminCategoryService";

function CreateCategoryPage() {

  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    name: "",
    description: "",
  });

  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {

    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });

  };

  const handleSubmit = async (e) => {

    e.preventDefault();

    try {

      setLoading(true);

      await createCategory(formData);

      alert(
        "Category created successfully"
      );

      navigate("/admin/categories");

    } catch (error) {

      console.error(error);

      alert(
        "Failed to create category"
      );

    } finally {

      setLoading(false);

    }
  };

  return (
    <MainLayout>

      <div className="max-w-xl mx-auto p-10">

        <h1 className="text-4xl font-bold mb-8">
          Create Category
        </h1>

        <form
          onSubmit={handleSubmit}
          className="space-y-4"
        >

          <input
            type="text"
            name="name"
            placeholder="Category Name"
            value={formData.name}
            onChange={handleChange}
            className="w-full border p-3 rounded"
          />

          <textarea
            name="description"
            placeholder="Description"
            value={formData.description}
            onChange={handleChange}
            className="w-full border p-3 rounded"
          />

          <button
            type="submit"
            disabled={loading}
            className="
              bg-blue-600
              text-white
              px-6
              py-3
              rounded
            "
          >
            {loading
              ? "Creating..."
              : "Create Category"}
          </button>

        </form>

      </div>

    </MainLayout>
  );
}

export default CreateCategoryPage;