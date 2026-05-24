/*/ SISTEMA DE INVENTARIO Y FACTURACIÓN
 INTEGRANTES 
 * LUCAS BALEN
 * LUNA CUESTO
 * ANDREA MARTINEZ 
 * ANGIE MESA **/


public class SistemaComercial {

    // BLOQUE PRINCIPAL (MAIN):
     
    public static void main(String[] args) {
       
        
        String[] inventarioProductos = {"Laptop Asus", "Mouse Logi", "Teclado Mec", "Monitor Dell", "Audífonos Sony"};
        int[] stockExistencias = {12, 45, 0, 8, 25}; 
        double[] preciosUnitarios = {2500000.0, 45000.0, 120000.0, 890000.0, 200000.0};

        
        String[] carritoCompras = {"Laptop Asus", "Teclado Mec", "Mouse Logi", "Silla Gamer"};
        int[] cantidadesDeseadas = {1, 2, 2, 1}; 

        System.out.println("=========================================================================");
        System.out.println("         INVENTARIO - INFORME"      );
        System.out.println("=========================================================================\n");

   
        System.out.println(">>> 1. ESTADO ACTUAL DEL INVENTARIO EN BODEGA:");
        mostrarInventarioCompleto(inventarioProductos, stockExistencias, preciosUnitarios);

     
        System.out.println("\n>>> 2. PROCESANDO CARRITO DE COMPRAS DEL CLIENTE...");
        procesarFacturacion(inventarioProductos, stockExistencias, preciosUnitarios, carritoCompras, cantidadesDeseadas);

        
        System.out.println("\n>>> 3. ESTADO ACTUALIZADO DEL INVENTARIO (POST-VENTA):");
        mostrarInventarioCompleto(inventarioProductos, stockExistencias, preciosUnitarios);
    }

    
    public static void mostrarInventarioCompleto(String[] productos, int[] stock, double[] precios) {
        System.out.println("-------------------------------------------------------------------------");
        System.out.printf("%-4s | %-18s | %-12s | %-16s | %-10s\n", "ID", "Descripción Item", "Existencias", "Precio Unitario", "Estado");
        System.out.println("-------------------------------------------------------------------------");
       
        
        for (int i = 0; i < productos.length; i++) {
           
            String estado = (stock[i] > 0) ? "Disponible" : "AGOTADO";
           
            
            System.out.printf("[%02d] | %-18s | %-12d | $%,-15.2f | %-10s\n",
                    (i + 1), productos[i], stock[i], precios[i], estado);
        }
        System.out.println("-------------------------------------------------------------------------");
    }

   
    public static void procesarFacturacion(String[] invProd, int[] invStock, double[] invPrecio, String[] carrito, int[] cants) {
      
        double subtotalBruto = 0.0;
        double impuestoIva = 0.0;
        double totalFinal = 0.0;
        final double TASA_IVA = 0.19; 

       
        String[] itemsFacturados = new String[carrito.length];
        int[] cantsFacturadas = new int[carrito.length];
        double[] subTotalesItems = new double[carrito.length];
        int contadorAprobados = 0; 

        for (int i = 0; i < carrito.length; i++) {
            String productoBuscado = carrito[i];
            int cantidadPedida = cants[i];

            
            int indiceInventario = buscarProductoPorNombre(invProd, productoBuscado);

           
            if (indiceInventario == -1) {
                System.out.println("  [ERROR RECHAZADO] '" + productoBuscado + "' no se encuentra en nuestro catálogo comercial.");
                continue; 
            }

            
            if (invStock[indiceInventario] == 0) {
                System.out.println("  [STOCK RECHAZADO] '" + productoBuscado + "' se encuentra agotado actualmente en bodega.");
                continue; // Salta a la siguiente iteración
            }

           
            if (invStock[indiceInventario] < cantidadPedida) {
                System.out.printf("  [AJUSTE LOGÍSTICO] Solicitó %d unidades de '%s', pero solo quedan %d. Se despachará lo disponible.\n",
                        cantidadPedida, productoBuscado, invStock[indiceInventario]);
                cantidadPedida = invStock[indiceInventario]; 
            }


            invStock[indiceInventario] -= cantidadPedida; 
            double costoCalculado = cantidadPedida * invPrecio[indiceInventario]; 

           
            itemsFacturados[contadorAprobados] = productoBuscado;
            cantsFacturadas[contadorAprobados] = cantidadPedida;
            subTotalesItems[contadorAprobados] = costoCalculado;
           
            subtotalBruto += costoCalculado; 
                        contadorAprobados++; 
        }

        
        if (contadorAprobados == 0) {
            System.out.println("\n[SISTEMA CAJA] Factura cancelada de forma automática: Ningún artículo superó los filtros logísticos.");
            return; 
        }

      
        impuestoIva = subtotalBruto * TASA_IVA;
        totalFinal = subtotalBruto + impuestoIva;

   
        imprimirTicketConsola(itemsFacturados, cantsFacturadas, subTotalesItems, contadorAprobados, subtotalBruto, impuestoIva, totalFinal);
    }

    
    public static int buscarProductoPorNombre(String[] catalogo, String consultaCliente) {
        for (int i = 0; i < catalogo.length; i++) {

            if (catalogo[i].equalsIgnoreCase(consultaCliente)) {
                return i; 
            }
        }
        return -1;

   
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