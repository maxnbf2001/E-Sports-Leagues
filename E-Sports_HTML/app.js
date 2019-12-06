var app = angular.module('esports', [])

app.component('main', {
    controller: MainController,
    templateUrl: "app.html"
});

function MainController()
{
    this.data = list;

    this.FIFA = 0;
    this.NBA = 1;
}