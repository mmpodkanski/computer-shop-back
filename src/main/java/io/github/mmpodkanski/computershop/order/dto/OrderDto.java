package io.github.mmpodkanski.computershop.order.dto;

import io.github.mmpodkanski.computershop.customer.dto.CustomerDto;

import java.util.Set;

public interface OrderDto {
    static OrderDto create(
            int id,
            double totalCost,
            Set<OrderItemDto>items,
            CustomerDto customer,
            String status
    ) {
        return new OrderDtoImpl(id, totalCost, items, customer, status);
    }

    int getId();

    double getTotalCost();

    Set<OrderItemDto> getItems();

    CustomerDto getCustomer();

    String getStatus();

    class OrderDtoImpl implements OrderDto {
        private final int id;
        private final double totalCost;
        private final Set<OrderItemDto>items;
        private final CustomerDto customer;
        private final String status;

        public OrderDtoImpl(final int id, final double totalCost, final Set<OrderItemDto> items, final CustomerDto customer, final String status) {
            this.id = id;
            this.totalCost = totalCost;
            this.items = items;
            this.customer = customer;
            this.status = status;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public double getTotalCost() {
            return totalCost;
        }

        @Override
        public Set<OrderItemDto> getItems() {
            return items;
        }

        @Override
        public CustomerDto getCustomer() {
            return customer;
        }

        @Override
        public String getStatus() {
            return status;
        }
    }
}