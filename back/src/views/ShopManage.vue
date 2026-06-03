<template>
  <div class="shop-manage">
    <h1 class="page-title">店铺管理</h1>
    
    <!-- 店铺信息 -->
    <div class="info-card">
      <div class="card-header">
        <h3>🏪 店铺信息</h3>
        <button class="btn-edit" @click="openEditModal">编辑</button>
      </div>
      <div class="info-grid">
        <div class="info-item"><label>店铺名称：</label>{{ shopInfo.shopName || '未设置' }}</div>
        <div class="info-item"><label>营业时间：</label>{{ shopInfo.hours || '09:00 - 20:00' }}</div>
        <div class="info-item"><label>联系电话：</label>{{ shopInfo.shopPhone || '未设置' }}</div>
        <div class="info-item"><label>店铺地址：</label>{{ shopInfo.shopAddress || '未设置' }}</div>
      </div>
    </div>
    
    <!-- 商品管理 -->
    <div class="info-card">
      <div class="card-header">
        <h3>🛒 商品管理</h3>
        <button class="btn-add" @click="openAddProductModal">+ 添加商品</button>
      </div>
      <div class="product-list">
        <div class="product-item" v-for="product in products" :key="product.id">
          <div class="product-info">
            <span class="product-name">{{ product.name }}</span>
            <span class="product-price">¥{{ product.price }}/{{ product.unit }}</span>
          </div>
          <div class="product-actions">
            <button class="btn-sm" @click="toggleProductStatus(product)">
              {{ product.status === 'on' ? '下架' : '上架' }}
            </button>
            <button class="btn-sm btn-danger" @click="deleteProduct(product)">删除</button>
          </div>
        </div>
        <div v-if="products.length === 0" class="empty-state">暂无商品，点击添加</div>
      </div>
    </div>
    
    <!-- 员工管理 -->
    <div class="info-card">
      <div class="card-header">
        <h3>👥 员工管理</h3>
        <button class="btn-add" @click="openAddEmployeeModal">+ 添加员工</button>
      </div>
      <div class="employee-list">
        <div class="employee-item" v-for="employee in employees" :key="employee.id">
          <span>{{ employee.icon || '👤' }} {{ employee.name }} · {{ employee.position }}</span>
          <button class="btn-sm btn-danger" @click="deleteEmployee(employee)">移除</button>
        </div>
        <div v-if="employees.length === 0" class="empty-state">暂无员工，点击添加</div>
      </div>
    </div>

    <!-- 编辑店铺信息弹窗 -->
    <div v-if="showEditModal" class="modal" @click.self="showEditModal = false">
      <div class="modal-content">
        <h3>编辑店铺信息</h3>
        <input type="text" v-model="editForm.shopName" placeholder="店铺名称" class="modal-input">
        <input type="text" v-model="editForm.hours" placeholder="营业时间（如：09:00-20:00）" class="modal-input">
        <input type="text" v-model="editForm.shopPhone" placeholder="联系电话" class="modal-input">
        <input type="text" v-model="editForm.shopAddress" placeholder="店铺地址" class="modal-input">
        <div class="modal-actions">
          <button class="btn-cancel" @click="showEditModal = false">取消</button>
          <button class="btn-submit" @click="saveShopInfo">保存</button>
        </div>
      </div>
    </div>

    <!-- 添加商品弹窗 -->
    <div v-if="showAddProductModal" class="modal" @click.self="showAddProductModal = false">
      <div class="modal-content">
        <h3>添加商品</h3>
        <input type="text" v-model="productForm.name" placeholder="商品名称" class="modal-input">
        <input type="number" v-model="productForm.price" placeholder="价格" class="modal-input" step="0.01">
        <input type="text" v-model="productForm.unit" placeholder="单位（如：针/次/袋）" class="modal-input">
        <div class="modal-actions">
          <button class="btn-cancel" @click="showAddProductModal = false">取消</button>
          <button class="btn-submit" @click="addProduct">添加</button>
        </div>
      </div>
    </div>

    <!-- 添加员工弹窗 -->
    <div v-if="showAddEmployeeModal" class="modal" @click.self="showAddEmployeeModal = false">
      <div class="modal-content">
        <h3>添加员工</h3>
        <input type="text" v-model="employeeForm.name" placeholder="员工姓名" class="modal-input">
        <input type="text" v-model="employeeForm.position" placeholder="职位（如：主治医师）" class="modal-input">
        <div class="modal-actions">
          <button class="btn-cancel" @click="showAddEmployeeModal = false">取消</button>
          <button class="btn-submit" @click="addEmployee">添加</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '../stores/user'
import { getProducts, addProduct as addProductApi, updateProduct, deleteProduct as deleteProductApi } from '../api/shop'
import { getEmployees, addEmployee as addEmployeeApi, deleteEmployee as deleteEmployeeApi } from '../api/shop'

const userStore = useUserStore()

// 店铺信息
const shopInfo = ref({
  shopName: '',
  hours: '09:00 - 20:00',
  shopPhone: '',
  shopAddress: ''
})

// 商品列表
const products = ref([])

// 员工列表
const employees = ref([])

// 编辑弹窗
const showEditModal = ref(false)
const editForm = ref({
  shopName: '',
  hours: '',
  shopPhone: '',
  shopAddress: ''
})

// 添加商品弹窗
const showAddProductModal = ref(false)
const productForm = ref({
  name: '',
  price: '',
  unit: ''
})

// 添加员工弹窗
const showAddEmployeeModal = ref(false)
const employeeForm = ref({
  name: '',
  position: ''
})

// 加载店铺信息
const loadShopInfo = () => {
  const user = userStore.currentUser
  if (user) {
    shopInfo.value.shopName = user.shopName || '未设置'
    shopInfo.value.shopPhone = user.shopPhone || '未设置'
    shopInfo.value.shopAddress = user.shopAddress || '未设置'
  }
}

// 保存店铺信息
const saveShopInfo = async () => {
  // TODO: 调用后端API更新店铺信息
  shopInfo.value = { ...editForm.value }
  alert('保存成功（演示模式）')
  showEditModal.value = false
}

const openEditModal = () => {
  editForm.value = { ...shopInfo.value }
  showEditModal.value = true
}

// 加载商品列表
const loadProducts = async () => {
  const shopId = userStore.currentUser?.id
  if (!shopId) return
  
  try {
    const res = await getProducts(shopId)
    if (res.code === 200) {
      products.value = res.data
    }
  } catch (error) {
    console.error('加载商品失败:', error)
  }
}

// 打开添加商品弹窗
const openAddProductModal = () => {
  productForm.value = { name: '', price: '', unit: '' }
  showAddProductModal.value = true
}

// 添加商品
const addProduct = async () => {
  if (!productForm.value.name || !productForm.value.price) {
    alert('请填写商品名称和价格')
    return
  }
  
  const shopId = userStore.currentUser?.id
  if (!shopId) return
  
  try {
    const res = await addProductApi({
      shopId: shopId,
      name: productForm.value.name,
      price: parseFloat(productForm.value.price),
      unit: productForm.value.unit || '件'
    })
    if (res.code === 200) {
      alert('商品已添加')
      showAddProductModal.value = false
      productForm.value = { name: '', price: '', unit: '' }
      loadProducts()
    } else {
      alert('添加失败')
    }
  } catch (error) {
    console.error('添加商品失败:', error)
    alert('添加失败')
  }
}

// 切换商品状态
const toggleProductStatus = async (product) => {
  const newStatus = product.status === 'on' ? 'off' : 'on'
  try {
    const res = await updateProduct({ ...product, status: newStatus })
    if (res.code === 200) {
      product.status = newStatus
      alert(`商品已${newStatus === 'on' ? '上架' : '下架'}`)
    } else {
      alert('操作失败')
    }
  } catch (error) {
    console.error('操作失败:', error)
    alert('操作失败')
  }
}

// 删除商品
const deleteProduct = async (product) => {
  if (!confirm(`确定要删除商品「${product.name}」吗？`)) return
  
  try {
    const res = await deleteProductApi(product.id)
    if (res.code === 200) {
      alert('商品已删除')
      loadProducts()
    } else {
      alert('删除失败')
    }
  } catch (error) {
    console.error('删除商品失败:', error)
    alert('删除失败')
  }
}

// 加载员工列表
const loadEmployees = async () => {
  const shopId = userStore.currentUser?.id
  if (!shopId) return
  
  try {
    const res = await getEmployees(shopId)
    if (res.code === 200) {
      employees.value = res.data
    }
  } catch (error) {
    console.error('加载员工失败:', error)
  }
}


// 打开添加员工弹窗
const openAddEmployeeModal = () => {
  employeeForm.value = { name: '', position: '' }
  showAddEmployeeModal.value = true
}
// 添加员工
const addEmployee = async () => {
  if (!employeeForm.value.name) {
    alert('请填写员工姓名')
    return
  }
  
  const shopId = userStore.currentUser?.id
  if (!shopId) return
  
  try {
    const res = await addEmployeeApi({
      shopId: shopId,
      name: employeeForm.value.name,
      position: employeeForm.value.position || '员工',
      icon: '👤'
    })
    if (res.code === 200) {
      alert('员工已添加')
      showAddEmployeeModal.value = false
      employeeForm.value = { name: '', position: '' }
      loadEmployees()
    } else {
      alert('添加失败')
    }
  } catch (error) {
    console.error('添加员工失败:', error)
    alert('添加失败')
  }
}

// 删除员工
const deleteEmployee = async (employee) => {
  if (!confirm(`确定要移除员工「${employee.name}」吗？`)) return
  
  try {
    const res = await deleteEmployeeApi(employee.id)
    if (res.code === 200) {
      alert('员工已移除')
      loadEmployees()
    } else {
      alert('移除失败')
    }
  } catch (error) {
    console.error('移除员工失败:', error)
    alert('移除失败')
  }
}

onMounted(() => {
  loadShopInfo()
  loadProducts()
  loadEmployees()
})
</script>

<style scoped>
.shop-manage { max-width: 900px; }
.page-title { font-size: 24px; margin-bottom: 24px; color: #1a3c34; }
.info-card { background: white; border-radius: 16px; padding: 20px; margin-bottom: 20px; box-shadow: 0 2px 8px rgba(0,0,0,0.05); }
.card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.card-header h3 { font-size: 18px; color: #1a3c34; }
.btn-edit, .btn-add { background: #2ecc71; color: white; border: none; padding: 6px 16px; border-radius: 20px; cursor: pointer; }
.btn-add { font-size: 13px; }
.info-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; }
.info-item { font-size: 14px; color: #555; }
.info-item label { font-weight: 600; }
.product-item, .employee-item { display: flex; justify-content: space-between; align-items: center; padding: 12px 0; border-bottom: 1px solid #eee; }
.product-name { font-weight: 500; margin-right: 12px; }
.product-price { color: #ff9800; font-size: 13px; }
.btn-sm { background: #f0f0f0; border: none; padding: 4px 12px; border-radius: 16px; cursor: pointer; margin-left: 8px; }
.btn-danger { background: #ff4757; color: white; }
.empty-state { text-align: center; padding: 30px; color: #999; }

/* 模态框 */
.modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}
.modal-content {
  background: white;
  border-radius: 20px;
  padding: 24px;
  width: 400px;
  max-width: 90%;
}
.modal-content h3 { margin-bottom: 20px; color: #1a3c34; }
.modal-input { width: 100%; padding: 12px; margin-bottom: 16px; border: 1px solid #ddd; border-radius: 12px; font-size: 14px; box-sizing: border-box; }
.modal-actions { display: flex; gap: 12px; justify-content: flex-end; }
.btn-cancel { background: #f0f0f0; border: none; padding: 10px 20px; border-radius: 24px; cursor: pointer; }
.btn-submit { background: #2ecc71; color: white; border: none; padding: 10px 20px; border-radius: 24px; cursor: pointer; }
</style>