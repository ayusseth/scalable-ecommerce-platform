import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

import MainLayout from "../../layouts/MainLayout";

import { getCategories } from "../../services/categoryService";

import {
  getProductById,
  updateProduct,
} from "../../services/adminProductService";

function EditProductPage() {

  const { id } = useParams();

  const navigate = useNavigate();

  const [loading, setLoading] =
    useState(true);

  const [categories, setCategories] =
    useState([]);

  const [formData, setFormData] =
    useState({
      name: "",
      description: "",
      categoryId: "",
      price: "",
      stockQuantity: "",
    });

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {

    try {

      const [
        product,
        categoriesData,
      ] = await Promise.all([
        getProductById(id),
        getCategories(),
      ]);

      setCategories(
        categoriesData
      );

      setFormData({
        name: product.name,
        description:
          product.description || "",
        categoryId:
          product.categoryId,
        price:
          product.price,
        stockQuantity:
          product.stockQuantity,
      });

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

      await updateProduct(
        id,
        {
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
        }
      );

      alert(
        "Product updated successfully"
      );

      navigate(
        "/admin/products"
      );

    } catch (error) {

      console.error(error);

      alert(
        "Failed to update product"
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
          Edit Product
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
            className="
              w-full
              border
              p-3
              rounded
            "
          />

          <textarea
            name="description"
            value={
              formData.description
            }
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
              bg-green-600
              text-white
              px-6
              py-3
              rounded
            "
          >
            Update Product
          </button>

        </form>

      </div>

    </MainLayout>
  );
}

export default EditProductPage;