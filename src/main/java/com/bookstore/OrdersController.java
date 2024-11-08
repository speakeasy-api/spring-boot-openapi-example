package com.bookstore;

import com.bookstore.Models.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@Tag(name = "Orders", description = "Order management APIs")
public class OrdersController {
    // In-memory storage for demo purposes
    private final HashMap<String, Order> orders = new HashMap<>();

    @Operation(summary = "Create a new order", description = "Create a new order for publications")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order created successfully",
                    content = @Content(schema = @Schema(implementation = Order.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<?> createOrder(
            @Parameter(description = "Order to be created", required = true)
            @RequestBody Order order) {
        // Validate order
        if (order.getItems() == null || order.getItems().isEmpty()) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse(400, "Order must contain at least one item"));
        }

        // Generate order ID and set initial status
        String orderId = "ord-" + UUID.randomUUID().toString().substring(0, 8);
        order.setId(orderId);
        order.setStatus(Models.OrderStatus.PENDING);

        // Calculate total price
        float totalPrice = order.getItems().stream()
            .map(Publication::getPrice)
            .reduce(0f, Float::sum);
        order.setTotalPrice(totalPrice);

        // Save order
        orders.put(orderId, order);
        return ResponseEntity.ok(order);
    }

    @Operation(summary = "Get an order by ID", description = "Retrieves an order's details by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = Order.class))),
        @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(
            @Parameter(description = "ID of the order to retrieve")
            @PathVariable String id) {
        Order order = orders.get(id);
        if (order == null) {
            return ResponseEntity.status(404)
                .body(new ErrorResponse(404, "Order not found"));
        }
        return ResponseEntity.ok(order);
    }

    @Operation(summary = "List all orders", description = "Get a list of all orders in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Order.class))))
    })
    @GetMapping
    public ResponseEntity<List<Order>> listOrders() {
        return ResponseEntity.ok(new ArrayList<>(orders.values()));
    }

    @Operation(summary = "Update order status", description = "Update the status of an existing order")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order status updated successfully",
                    content = @Content(schema = @Schema(implementation = Order.class))),
        @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid status",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(
            @Parameter(description = "ID of the order to update")
            @PathVariable String id,
            @Parameter(description = "New status for the order", required = true)
            @RequestParam Models.OrderStatus status) {
        Order order = orders.get(id);
        if (order == null) {
            return ResponseEntity.status(404)
                .body(new ErrorResponse(404, "Order not found"));
        }

        // Validate status transition
        if (!isValidStatusTransition(order.getStatus(), status)) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse(400, "Invalid status transition from " + 
                    order.getStatus() + " to " + status));
        }

        order.setStatus(status);
        return ResponseEntity.ok(order);
    }

    @Operation(summary = "Cancel an order", description = "Cancel an existing order")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order cancelled successfully",
                    content = @Content(schema = @Schema(implementation = Order.class))),
        @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "400", description = "Order cannot be cancelled",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelOrder(
            @Parameter(description = "ID of the order to cancel")
            @PathVariable String id) {
        Order order = orders.get(id);
        if (order == null) {
            return ResponseEntity.status(404)
                .body(new ErrorResponse(404, "Order not found"));
        }

        if (order.getStatus() == Models.OrderStatus.DELIVERED) {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse(400, "Cannot cancel a delivered order"));
        }

        order.setStatus(Models.OrderStatus.CANCELLED);
        return ResponseEntity.ok(order);
    }

    // Helper method to validate status transitions
    private boolean isValidStatusTransition(Models.OrderStatus currentStatus, Models.OrderStatus newStatus) {
        if (currentStatus == newStatus) {
            return true;
        }

        switch (currentStatus) {
            case PENDING:
                return newStatus == Models.OrderStatus.SHIPPED || 
                       newStatus == Models.OrderStatus.CANCELLED;
            case SHIPPED:
                return newStatus == Models.OrderStatus.DELIVERED;
            case DELIVERED:
            case CANCELLED:
                return false;
            default:
                return false;
        }
    }
}