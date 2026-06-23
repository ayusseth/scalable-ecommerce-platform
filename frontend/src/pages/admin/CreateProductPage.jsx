import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import MainLayout from "../../layouts/MainLayout";

import { getCategories } from "../../services/categoryService";
import { createProduct } from "../../services/adminProductService";

function CreateProductPage() {

  const navigate = useNavigate();

  const [categories, setCategories] =
    useState([]);

  const [loading, setLoading] =
    useState(true);

  const [formData, setFormData] =
    useState({
      name: "",
      description: "",
      categoryId: "",
      price: "",
      stockQuantity: "",
    });

  useEffect(() => {
    loadCategories();
  }, []);

  const loadCategories = async () => {
    try {

      const data =
        await getCategories();

      setCategories(data);

    } catch (error) {

      console.error(error);

    } finally {

      setLoading(false);

    }
  };

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

      await createProduct({
        ...formData,
        categoryId:
          Number(
            formData.categoryId
          ),
        price:
          Number(
            formData.price
          ),
        stockQuantity:
          Number(
            formData.stockQuantity
          ),
      });

      alert(
        "Product created successfully"
      );

      navigate(
        "/admin/products"
      );

    } catch (error) {

      console.error(error);

      alert(
        "Failed to create product"
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

      <div className="max-w-3xl mx-auto p-10">

        <h1 className="text-4xl font-bold mb-8">
          Create Product
        </h1>

        <form
          onSubmit={handleSubmit}
          className="space-y-4"
        >

          <input
            type="text"
            name="name"
            placeholder="Product Name"
            value={formData.name}
            onChange={handleChange}
            className="
              w-full
              border
              p-3
              rounded
            "
          />

          <textarea
            name="description"
            placeholder="Description"
            value={formData.description}
            onChange={handleChange}
            className="
              w-full
              border
              p-3
              rounded
            "
          />

          <select
            name="categoryId"
            value={
              formData.categoryId
            }
            onChange={handleChange}
            className="
              w-full
              border
              p-3
              rounded
            "
          >

            <option value="">
              Select Category
            </option>

            {categories.map(
              (category) => (
                <option
                  key={category.id}
                  value={category.id}
                >
                  {category.name}
                </option>
              )
            )}

          </select>

          <input
            type="number"
            name="price"
            placeholder="Price"
            value={formData.price}
            onChange={handleChange}
            className="
              w-full
              border
              p-3
              rounded
            "
          />

          <input
            type="number"
            name="stockQuantity"
            placeholder="Stock Quantity"
            value={
              formData.stockQuantity
            }
            onChange={handleChange}
            className="
              w-full
              border
              p-3
              rounded
            "
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
            Create Product
          </button>

        </form>

      </div>

    </MainLayout>
  );
}

export default CreateProductPage;