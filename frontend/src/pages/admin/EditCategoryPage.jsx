import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

import MainLayout from "../../layouts/MainLayout";

import {
  getCategoryById,
  updateCategory,
} from "../../services/adminCategoryService";

function EditCategoryPage() {

  const { id } = useParams();

  const navigate = useNavigate();

  const [loading, setLoading] = useState(true);

  const [formData, setFormData] = useState({
    name: "",
    description: "",
  });

  useEffect(() => {
    loadCategory();
  }, []);

  const loadCategory = async () => {

    try {

      const category =
        await getCategoryById(id);

      setFormData({
        name: category.name,
        description:
          category.description || "",
      });

    } catch (error) {

      console.error(error);

      alert(
        "Failed to load category"
      );

    } finally {

      setLoading(false);

    }
  };

  const handleChange = (e) => {

    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });

  };

  const handleSubmit = async (e) => {

    e.preventDefault();

    try {

      await updateCategory(
        id,
        formData
      );

      alert(
        "Category updated successfully"
      );

      navigate("/admin/categories");

    } catch (error) {

      console.error(error);

      alert(
        "Failed to update category"
      );

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

      <div className="max-w-xl mx-auto p-10">

        <h1 className="text-4xl font-bold mb-8">
          Edit Category
        </h1>

        <form
          onSubmit={handleSubmit}
          className="space-y-4"
        >

          <input
            type="text"
            name="name"
            value={formData.name}
            onChange={handleChange}
            className="w-full border p-3 rounded"
          />

          <textarea
            name="description"
            value={formData.description}
            onChange={handleChange}
            className="w-full border p-3 rounded"
          />

          <button
            type="submit"
            className="
              bg-blue-600
              text-white
              px-6
              py-3
              rounded
            "
          >
            Update Category
          </button>

        </form>

      </div>

    </MainLayout>
  );
}

export default EditCategoryPage;