var app = angular.module('esports', [])

app.component('main', {
    controller: MainController,
    templateUrl: "E-Sports_HTML/app.html"
});

function MainController()
{
    this.info = websiteData;
    this.FIFA = 0;
    this.NBA = 1;

    this.shown = -1;
}