import { useEffect, useState } from "react";
import MainLayout from "../../layouts/MainLayout";
import { getProducts } from "../../services/productService";
import ProductCard from "../../components/ProductCard";

function ProductsPage() {

  const [products, setProducts] = useState([]);

useEffect(() => {

  const fetchProducts = async () => {

    try {

      const data = await getProducts();

        console.log("API Response:", data);
      setProducts(data);

    } catch (error) {

      console.error(error);

    }

  };

  fetchProducts();

}, []);

  

  return (
    <MainLayout>

      <div className="max-w-7xl mx-auto p-8">

        <h1 className="text-4xl font-bold mb-8">
          Products
        </h1>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">

          {products.map(product => (

            <ProductCard
    key={product.id}
    product={product}
  />

          ))}

        </div>

      </div>

    </MainLayout>
  );
}

export default ProductsPage;