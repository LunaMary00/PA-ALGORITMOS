/*/ SISTEMA DE INVENTARIO Y FACTURACIÓN
 INTEGRANTES 
 * LUCAS BALEN
 * LUNA CUESTO
 * ANDREA MARTINEZ 
 * ANGIE MESA **/

/**
 * Sistema de simulación comercial para la gestión de inventarios y facturación.
 * Permite listar productos, procesar carritos de compras y actualizar stock.
 */

public class SistemaComercial {
 
  /**
     * Punto de entrada principal del programa. 
     * Inicializa el inventario base y simula la compra de un cliente.
     */
 
    // BLOQUE PRINCIPAL (MAIN):
     
    public static void main(String[] args) {
       
        
        String[] inventarioProductos = {"Laptop Asus", "Mouse Logi", "Teclado Mec", "Monitor Dell", "Audífonos Sony"};
        // Datos base del inventario
        int[] stockExistencias = {12, 45, 0, 8, 25}; 
        double[] preciosUnitarios = {2500000.0, 45000.0, 120000.0, 890000.0, 200000.0};

        // Datos del carrito del cliente
        String[] carritoCompras = {"Laptop Asus", "Teclado Mec", "Mouse Logi", "Silla Gamer"};
        int[] cantidadesDeseadas = {1, 2, 2, 1}; 

        System.out.println("=========================================================================");
        System.out.println("         INVENTARIO - INFORME"      );
        System.out.println("=========================================================================\n");

        // Primero mostrar estado inicial del inventario
        System.out.println(">>> 1. ESTADO ACTUAL DEL INVENTARIO EN BODEGA:");
        mostrarInventarioCompleto(inventarioProductos, stockExistencias, preciosUnitarios);

        // Segundo procesar la venta y generar factura  
        System.out.println("\n>>> 2. PROCESANDO CARRITO DE COMPRAS DEL CLIENTE...");
        procesarFacturacion(inventarioProductos, stockExistencias, preciosUnitarios, carritoCompras, cantidadesDeseadas);

        // Tercero mostrar estado final reflejando las deducciones de stock
        System.out.println("\n>>> 3. ESTADO ACTUALIZADO DEL INVENTARIO (POST-VENTA):");
        mostrarInventarioCompleto(inventarioProductos, stockExistencias, preciosUnitarios);
    }
 
    /**
     * Imprime en consola la lista completa de productos con su formato visual.
     */
 
    public static void mostrarInventarioCompleto(String[] productos, int[] stock, double[] precios) {
        System.out.println("-------------------------------------------------------------------------");
        System.out.printf("%-4s | %-18s | %-12s | %-16s | %-10s\n", "ID", "Descripción Item", "Existencias", "Precio Unitario", "Estado");
        System.out.println("-------------------------------------------------------------------------");
       
        
        for (int i = 0; i < productos.length; i++) {
        // Operador ternario para determinar disponibilidad
            String estado = (stock[i] > 0) ? "Disponible" : "AGOTADO";
           
        // Impresión formateada con alineación y separadores de miles    
            System.out.printf("[%02d] | %-18s | %-12d | $%,-15.2f | %-10s\n",
                    (i + 1), productos[i], stock[i], precios[i], estado);
        }
        System.out.println("-------------------------------------------------------------------------");
    }
    /**
     * Evalúa las reglas de negocio, descuenta existencias y calcula los impuestos de la compra.
     */
   
    public static void procesarFacturacion(String[] invProd, int[] invStock, double[] invPrecio, String[] carrito, int[] cants) {
      
        double subtotalBruto = 0.0;
        double impuestoIva = 0.0;
        double totalFinal = 0.0;
        final double TASA_IVA = 0.19; // IVA Estándar (19%)

        // Estructuras temporales para almacenar solo los productos que aprueben los filtros
        String[] itemsFacturados = new String[carrito.length];
        int[] cantsFacturadas = new int[carrito.length];
        double[] subTotalesItems = new double[carrito.length];
        int contadorAprobados = 0; 

     
        for (int i = 0; i < carrito.length; i++) {
            String productoBuscado = carrito[i];
            int cantidadPedida = cants[i];

            // Buscar producto en el catálogo
            int indiceInventario = buscarProductoPorNombre(invProd, productoBuscado);

            // Filtro 1: Producto inexistente
            if (indiceInventario == -1) {
                System.out.println("  [ERROR RECHAZADO] '" + productoBuscado + "' no se encuentra en nuestro catálogo comercial.");
                continue; 
            }

            // Filtro 2: Producto sin existencias
            if (invStock[indiceInventario] == 0) {
                System.out.println("  [STOCK RECHAZADO] '" + productoBuscado + "' se encuentra agotado actualmente en bodega.");
                continue; // Salta a la siguiente iteración
            }

            // Aplicar cambios transaccionales
            invStock[indiceInventario] -= cantidadPedida; 
            double costoCalculado = cantidadPedida * invPrecio[indiceInventario]; 

            // Registrar ítem aprobado en la lista de facturación
            itemsFacturados[contadorAprobados] = productoBuscado;
            cantsFacturadas[contadorAprobados] = cantidadPedida;
            subTotalesItems[contadorAprobados] = costoCalculado;
           
            subtotalBruto += costoCalculado; 
                        contadorAprobados++; 
        }
     
        // Cálculos finales financieros
        impuestoIva = subtotalBruto * TASA_IVA;
        totalFinal = subtotalBruto + impuestoIva;

        // Renderizar el recibo final
        imprimirTicketConsola(itemsFacturados, cantsFacturadas, subTotalesItems, contadorAprobados, subtotalBruto, impuestoIva, totalFinal);
    }
    /**
     * Realiza una búsqueda secuencial ignorando mayúsculas/minúsculas.
     * @return Índice numérico si existe, de lo contrario -1.
     */
    
    public static int buscarProductoPorNombre(String[] catalogo, String consultaCliente) {
        for (int i = 0; i < catalogo.length; i++) {

            if (catalogo[i].equalsIgnoreCase(consultaCliente)) {
                return i; 
            }
        }
        return -1;
        }
     /**
     * Genera la representación impresa en formato de ticket para el usuario.
     */
   
    public static void imprimirTicketConsola(String[] prods, int[] cants, double[] subTotales, int limite, double sub, double iva, double total) {
        System.out.println("\n=========================================================================");
        System.out.println("                      FACTURA ELECTRÓNICA DE VENTA                       ");
        System.out.println("=========================================================================");
        System.out.printf("%-20s | %-10s | %-20s\n", "Artículo", "Cant.", "Subtotal Línea");
        System.out.println("-------------------------------------------------------------------------");
       
       
        for (int i = 0; i < limite; i++) {
            System.out.printf("%-20s | %-10d | $%,-19.2f\n", prods[i], cants[i], subTotales[i]);
        }
       
        System.out.println("=========================================================================");
        System.out.printf("  SUBTOTAL BRUTO DE COMPRA:                     $%,.2f\n", sub);
        System.out.printf("  IVA REGULADO DE LEY (19.0%%):                   $%,.2f\n", iva);
        System.out.println("-------------------------------------------------------------------------");
        System.out.printf("  TOTAL NETO A TRANSFERIR:                      $%,.2f\n", total);
        System.out.println("=========================================================================");
        System.out.println("             ¡GRACIAS POR CONFIAR EN NUESTROS SERVICIOS!                ");
        System.out.println("=========================================================================\n");
    }
}
