```mermaid
---
title: classes 
---
classDiagram
	note "This is the logic layer,\nanywhere there might be a DS ( data structure ) \nwould imply a data class for it" 
	Vendor <|-- User : is the parent of
	Planner <|-- User : is the parent of  
	Event  <|-- Planner : has a DS of 
	Vendor <|-- Event : has a DS of 
	Event --|> GuestList : has a 
	GuestList --|> Guest : has a DS of 
	Event --|> Task : has a DS of 
	Invoice --|> CostItem : has a DS of 
	User --|> Profile : has a
	Vendor --|> Request : has a DS of 
	Request --|> Vendor : has a 
	Request --|> Event : has a 
	class User {
		+data structure for eventS
		+Profile profile 
	}
	class Vendor {
		+service 
		+data structure of requests
	}
	class Event {
		+budget 
		+vendor data structure ( map ? ) 
		+tasks data structure 
		+GuestList guestList 
	}
	class GuestList {
		+guest data structure
	}
	class Guest {
		+string name 
		+food restrictions data structure 
	}
	class Task {
		+timeScheduleCompletion 
		+bool completed 
		+bool late
	}
	class Profile {
		+star rating 
		+data structure for written reviews
	}
	class Invoice {
		+CostItem data structure 
	}
	class CostItem {
		+String serviceDescription 
		+float costInCDN
	}
	class Request { 
		+Vendor vendor
		+Event event 
	}
