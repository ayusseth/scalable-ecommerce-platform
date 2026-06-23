import { useEffect, useState } from "react";

import { Link } from "react-router-dom";

import MainLayout from "../../layouts/MainLayout";

import {
  getProducts,
  deleteProduct,
} from "../../services/adminProductService";

function AdminProductsPage() {

  const [products, setProducts] =
    useState([]);

  const [loading, setLoading] =
    useState(true);

  useEffect(() => {
    loadProducts();
  }, []);

  const loadProducts = async () => {
    try {

      const data =
        await getProducts();

      setProducts(data);

    } catch (error) {

      console.error(error);

    } finally {

      setLoading(false);

    }
  };

  const handleDelete = async (id) => {

    const confirmed = window.confirm(
      "Delete this product?"
    );

    if (!confirmed) return;

    try {

      await deleteProduct(id);

      await loadProducts();

      alert(
        "Product deleted successfully"
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
      <div className="max-w-7xl mx-auto p-10">

        <div className="flex justify-between mb-8">

          <h1 className="text-4xl font-bold">
            Products
          </h1>

          <Link
            to="/admin/products/new"
            className="
              bg-blue-600
              text-white
              px-4
              py-2
              rounded
            "
          >
            Create Product
          </Link>

        </div>

        <div className="space-y-4">

          {products.map((product) => (

            <div
              key={product.id}
              className="
                border
                rounded-lg
                p-5
              "
            >

              <div className="flex justify-between">

                <div>

                  <h2 className="font-bold">
                    {product.name}
                  </h2>

                  <p>
                    ₹ {product.price}
                  </p>

                  <p>
                    Category:
                    {" "}
                    {product.categoryName}
                  </p>

                  <p>
                    Stock:
                    {" "}
                    {product.stockQuantity}
                  </p>

                  {product.stockQuantity <
                    10 && (
                    <span
                      className="
                        text-red-600
                        font-bold
                      "
                    >
                      Low Stock
                    </span>
                  )}

                </div>

                <div className="flex gap-3">

                  <Link
                    to={`/admin/products/${product.id}/edit`}
                    className="
                      bg-yellow-500
                      text-white
                      px-4
                      py-2
                      rounded
                    "
                  >
                    Edit
                  </Link>

                  <button
                    onClick={() =>
                      handleDelete(
                        product.id
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

            </div>

          ))}

        </div>

      </div>
    </MainLayout>
  );
}

export default AdminProductsPage;