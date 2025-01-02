# Architecture Model 

## Iteration 3 Diagram

![Iteration 1 Architecture Diagram](./Artifacts/Iteration%203/Architecture.png)

## Architecture Overview

Our project's architecture is designed to resemble a three-tier structure, encompassing an interface layer, a logic layer, and a storage layer.

The interface layer, contained within the presentation folder, remains our largest directory with key files such as CreateAccountActivity.java, LoginActivity.java, and SelectUserTypeActivity.java, enabling users to select a role as a vendor or a planner, create an account, and log into it. The AddEventActivity permits users to input new events, while EditEventActivity.Java allows for alterations or deletions of existing events. PlannerMainActivity serves as the central hub for planners, displaying and facilitating modifications to events.

For vendor interaction, the VendorCategoryActivity aids in the categorization of services, leading into VendorListActivity which lists vendors based on the chosen categories. VendorProfileActivity presents detailed vendor profiles. VendorMainActivity is now the main interface for vendors, listing accepted service requests, with ServiceRequestDetailsActivity showing details for service request management. UnresolvedServiceRequestsActivity compiles incoming requests, and a new addition, PlannerProfileDetailsActivity, is included for detailed planner profile management. Furthermore, InvoiceFragment has been incorporated for handling invoices.

The logic layer is housed within the logic folder, with EventManager, VendorManager, and UserManager maintaining their critical roles in orchestrating the flow between the interface and storage layers. They are now joined by RequestInvoiceManager, which manages invoice-related data and interactions.

The persistence folder represents the storage layer, with the new addition of InvoicePersistence corresponding to the InvoiceFragment. This layer secures data concerning events, vendors, service requests, and user accounts, systematically aligning with the logic layer's activities.

Lastly, the objects folder forms the foundation of our architecture, housing classes like Event.Java, ServiceRequest.Java, Vendor.Java, and User.Java. These objects encapsulate crucial details pertaining to their respective entities, ensuring the application's data integrity and structure.

