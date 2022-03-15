## 3. Type Erasure

*Type Erasure* là một mapping (ánh xạ) từ type (có thể gồm parameterized types & type variables) sang type (không bao gồm parameterized types & type variables).  

Erasure của type T được ký hiệu: |T|  

Erasure mapping được định nghĩa như sau:  

- The erasure của một *parameterized type* G<T1,...,Tn> là |G|.  
- The erasure của một *nested type* T.C là |T|.C.  
- The erasure của một *array type* T[] là |T|[].  
- The erasure của một *type variable* là the erasure của ràng buộc tận cùng bên trái (leftmost bound) của nó.  
- The erasure của *every other type* là chính type đó.  

*Type Erasure* cũng ánh xạ signature của một method hoặc constructor với một signature không có parameterized types or type variables. The erasure của một method hoặc constructor signature s là một signature bao gồm tên giống với s và erasures của tất cả các parameter types chính thức được đưa ra trong s.  

Return type của một method và các type parameters của một generic method or constructor cũng bị xóa nếu signature của method or constructor bị xóa.  

The erasure của signature của một generic method không có type parameters.


## 4. Subtyping 

- T là *subtype* của S          (T <: S),  
- T là *direct subtype* của S   (T <1 S),  
- T là *proper subtype* của S   (T < S),  