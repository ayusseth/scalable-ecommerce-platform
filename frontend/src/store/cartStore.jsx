import { createContext, useContext, useState } from "react";

const CartContext = createContext();

export function CartProvider({ children }) {

  const [cartItems, setCartItems] = useState([]);

  const addToCart = (product) => {

  setCartItems((prev) => {

    const existingItem = prev.find(
      (item) => item.product.id === product.id
    );

    if (existingItem) {

      return prev.map((item) =>
        item.product.id === product.id
          ? {
              ...item,
              quantity: item.quantity + 1,
            }
          : item
      );

    }

    return [
      ...prev,
      {
        product,
        quantity: 1,
      },
    ];

  });

};

const removeFromCart = (productId) => {

  setCartItems((prev) =>
    prev.filter(
      (item) => item.product.id !== productId
    )
  );

};

const increaseQuantity = (productId) => {

  setCartItems((prev) =>
    prev.map((item) =>
      item.product.id === productId
        ? {
            ...item,
            quantity: item.quantity + 1,
          }
        : item
    )
  );

};

const decreaseQuantity = (productId) => {

  setCartItems((prev) =>
    prev
      .map((item) =>
        item.product.id === productId
          ? {
              ...item,
              quantity: item.quantity - 1,
            }
          : item
      )
      .filter((item) => item.quantity > 0)
  );

};

  return (
    <CartContext.Provider
      value={{
  cartItems,
  addToCart,
  removeFromCart,
  increaseQuantity,
  decreaseQuantity,
}}
    >
      {children}
    </CartContext.Provider>
  );
}

export const useCart = () => useContext(CartContext);